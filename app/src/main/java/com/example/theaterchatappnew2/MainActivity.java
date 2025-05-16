package com.example.theaterchatappnew2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.UUID;

public class MainActivity extends BaseActivity {

    private EditText messageInput;
    private Button sendButton;
    private boolean awaitingComplaint = false;

    private TextView chatMessages;
    private ScrollView scrollView;

    private boolean awaitingShowChoice = false;
    private boolean awaitingTimeChoice = false;
    private boolean awaitingSeatInput = false;
    private boolean awaitingConfirmation = false;

    private String pendingShow = null;
    private String pendingTime = null;
    private int requestedSeats = 1;

    private String currentUserName;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";

    private int unknownCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Init shared seat state
        ShowManager.init(getApplicationContext());

        setupBottomNav();

        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatMessages = findViewById(R.id.chatMessages);
        scrollView = findViewById(R.id.scrollView);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().remove(KEY_USERNAME).apply(); // simulate logout
        currentUserName = null;

        promptForUserName(prefs);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                appendChat("You: " + userMessage);
                processUserMessage(userMessage.toLowerCase());
                messageInput.setText("");
            }
        });
    }
    private void saveComplaint(String user, String complaintText) {
        SharedPreferences complaintPrefs = getSharedPreferences("ComplaintPrefs", MODE_PRIVATE);
        String existing = complaintPrefs.getString(user, "");
        String updated = existing + "\n- " + complaintText;
        complaintPrefs.edit().putString(user, updated).apply();
    }


    private void promptForUserName(SharedPreferences prefs) {
        EditText input = new EditText(this);
        input.setHint("Enter your name");

        new AlertDialog.Builder(this)
                .setTitle("Welcome!")
                .setMessage("Please enter your name:")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    currentUserName = input.getText().toString().trim();
                    if (currentUserName.isEmpty()) currentUserName = "Guest";
                    BookingManager.init(getApplicationContext(), currentUserName);
                    appendChat(" Hello, " + currentUserName + "! Let's get started.");
                })
                .show();
    }

    private void processUserMessage(String message) {
        boolean known = false;

        if (message.contains("cancel")) {
            known = true;
            List<BookingInfo> bookings = BookingManager.getBookings();
            boolean found = false;
            for (BookingInfo booking : bookings) {
                if (message.contains(booking.code.toLowerCase())) {
                    BookingManager.removeBookingByCode(booking.code);
                    appendChat("Booking with code " + booking.code + " has been cancelled.");
                    found = true;
                    break;
                }
            }
            if (!found) {
                appendChat("No booking found with the provided code.");
                appendChat("Here are your active bookings:");
                for (BookingInfo b : bookings) {
                    appendChat("- " + b.title + " at " + b.datetime + " | Code: " + b.code);
                }
            }
            resetBookingState();
            return;
        }

        if (message.contains("book") || message.contains("reserve")) {
            known = true;
            awaitingShowChoice = true;
            appendChat("Which show would you like to book?\n- Hamlet\n- Romeo");
            return;
        }
        if (message.equals("clear all")) {
            BookingManager.clearAllBookings();
            ShowManager.resetAll();
            appendChat(" All bookings and seat availabilities have been reset.");
            return;
        }

        if (awaitingShowChoice) {
            known = true;
            if (message.contains("hamlet")) {
                pendingShow = "hamlet";
            } else if (message.contains("romeo")) {
                pendingShow = "romeo";
            } else {
                appendChat("Please choose 'Hamlet' or 'Romeo'.");
                return;
            }

            awaitingShowChoice = false;
            awaitingTimeChoice = true;

            String msg = "Available times for " + capitalize(pendingShow) + ":\n";
            for (String time : new String[]{"18:00", "21:00"}) {
                int seats = ShowManager.getAvailableSeats(pendingShow, time);
                msg += "- " + time + " (" + seats + " seats left)\n";
            }
            appendChat(msg + "Which time would you prefer?");
            return;
        }

        if (awaitingTimeChoice) {
            known = true;
            if (message.contains("18")) {
                pendingTime = "18:00";
            } else if (message.contains("21")) {
                pendingTime = "21:00";
            } else {
                appendChat("Please choose between 18:00 or 21:00.");
                return;
            }

            int seats = ShowManager.getAvailableSeats(pendingShow, pendingTime);
            if (seats == 0) {
                appendChat("No seats left for that time. Try another.");
                return;
            }

            awaitingTimeChoice = false;
            awaitingSeatInput = true;
            appendChat("How many seats would you like to book for " + capitalize(pendingShow) + " at " + pendingTime + "? (" + seats + " available)");
            return;
        }

        if (awaitingSeatInput) {
            known = true;
            try {
                requestedSeats = Integer.parseInt(message);
                int available = ShowManager.getAvailableSeats(pendingShow, pendingTime);
                if (requestedSeats > available) {
                    appendChat("Only " + available + " seats are available. Try fewer.");
                    return;
                }
                awaitingSeatInput = false;
                awaitingConfirmation = true;
                appendChat("Confirm booking " + requestedSeats + " seats for " + capitalize(pendingShow) + " at " + pendingTime + "? (yes/no)");
            } catch (NumberFormatException e) {
                appendChat("Please enter a valid number.");
            }
            return;
        }

        if (awaitingConfirmation) {
            known = true;
            if (message.contains("yes")) {
                boolean booked = ShowManager.bookSeats(pendingShow, pendingTime, requestedSeats);
                if (booked) {
                    String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                    String seatInfo = ShowManager.generateSeats(pendingShow, pendingTime, requestedSeats);
                    BookingInfo booking = new BookingInfo(
                            capitalize(pendingShow),
                            pendingTime,
                            seatInfo,
                            code,
                            currentUserName,
                            requestedSeats
                    );
                    BookingManager.addBooking(booking);
                    appendChat(" Booking confirmed!\nShow: " + capitalize(pendingShow) + "\nTime: " + pendingTime + "\nSeats: " + seatInfo + "\nCode: " + code);
                } else {
                    appendChat(" Booking failed. Seats may no longer be available.");
                }
            } else {
                appendChat("Booking cancelled.");
            }
            resetBookingState();
            return;
        }

        if (message.contains("bookings")) {
            known = true;
            List<BookingInfo> bookings = BookingManager.getBookings();
            if (bookings.isEmpty()) {
                appendChat("You have no bookings.");
            } else {
                StringBuilder builder = new StringBuilder();
                for (BookingInfo booking : bookings) {
                    builder.append(" ").append(booking.title)
                            .append(" at ").append(booking.datetime)
                            .append(" | Seats: ").append(booking.seat)
                            .append(" | Code: ").append(booking.code)
                            .append("\n");
                }
                appendChat(builder.toString().trim());
            }
            return;
        }
//  Πληροφορίες για παραστάσεις
        if (message.contains("info") || message.contains("theater") || message.contains("show") || message.contains("play")) {
            known = true;
            appendChat(" Our theater features two plays:\n" +
                    "- Hamlet: A classic tragedy by Shakespeare.\n" +
                    "- Romeo: A romantic drama about forbidden love.\n" +
                    "Each is performed at 18:00 and 21:00 daily in two separate halls.");
            return;
        }

//  Παράπονα
        if (message.contains("complaint") || message.contains("problem") || message.contains("issue") || message.contains("angry")) {
            known = true;
            awaitingComplaint = true;
            appendChat("We're sorry to hear that. Please describe your issue and it will be forwarded to our support team.");
            // (Μπορείς αργότερα να το αποθηκεύεις ή να ανοίγεις νέο activity)
            return;
        }
        // ✍️ Αν περιμένουμε παράπονο, το αποθηκεύουμε
        if (awaitingComplaint) {
            saveComplaint(currentUserName, message);
            appendChat(" Thank you! We filed your complaint. Our support team will look into it.\n" +
                    " For more help, you may contact the box office at 699999999.");
            awaitingComplaint = false;
            return;
        }

//  Επικοινωνία με υπάλληλο
        if (message.contains("talk") || message.contains("human") || message.contains("representative") || message.contains("someone") || message.contains("contact")) {
            known = true;
            appendChat(" You can contact a representative at the box office at: 6999999999");
            return;
        }

        // fallback
        if (!known) {
            unknownCount++;
            appendChat("I didn't understand. Try: 'book', 'cancel ABC123', or 'bookings'.");
            if (unknownCount >= 3) {
                appendChat("It seems you're having trouble. Would you like to speak to a human at the box office?");
                unknownCount = 0;
            }
        } else {
            unknownCount = 0;
        }
    }

    private void resetBookingState() {
        pendingShow = null;
        pendingTime = null;
        requestedSeats = 1;
        awaitingShowChoice = false;
        awaitingTimeChoice = false;
        awaitingSeatInput = false;
        awaitingConfirmation = false;
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private void appendChat(String message) {
        chatMessages.append("\n" + message);
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }

    @Override
    protected String getBottomNavType() {
        return "home";
    }
}
