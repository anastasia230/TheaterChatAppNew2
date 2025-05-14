from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Sample show data with seat availability per time
shows = {
    "Hamlet": {
        "hall": "Hall A",
        "times": {
            "6:00 PM": 50,
            "9:00 PM": 50
        }
    },
    "Antigone": {
        "hall": "Hall B",
        "times": {
            "5:00 PM": 50,
            "8:00 PM": 50
        }
    }
}

# Simple user session memory
user_states = {
    "current_step": None,
    "selected_show": None,
    "selected_time": None,
    "ticket_count": None
}

@app.route('/chatbot', methods=['POST'])
def chatbot():
    data = request.get_json()
    user_message = data.get("message", "").strip().lower()

    # Show info
    if any(word in user_message for word in ["show", "playing", "what is on", "schedule", "performances", "today"]):
        reply = "Today's performances:\n"
        for show, info in shows.items():
            times = ", ".join([f"{time} ({seats} seats)" for time, seats in info['times'].items()])
            reply += f"{show} ({info['hall']}): {times}\n"
        return jsonify({"reply": reply})

    # Start booking
    if any(word in user_message for word in ["book", "ticket", "reserve"]):
        user_states["current_step"] = "awaiting_show"
        return jsonify({"reply": "Sure! Which show would you like to book tickets for? (e.g., Hamlet or Antigone)"})

    # Step: select show
    if user_states["current_step"] == "awaiting_show":
        selected = next((show for show in shows if show.lower() in user_message), None)
        if selected:
            user_states["selected_show"] = selected
            user_states["current_step"] = "awaiting_time"
            available_times = ", ".join(shows[selected]["times"].keys())
            return jsonify({"reply": f"{selected} is playing at {available_times}. What time would you prefer?"})
        else:
            return jsonify({"reply": "I didn't catch that. Please choose between Hamlet or Antigone."})

    # Step: select time
    if user_states["current_step"] == "awaiting_time":
        show = user_states["selected_show"]
        for t in shows[show]["times"]:
            if t.lower() in user_message:
                user_states["selected_time"] = t
                user_states["current_step"] = "awaiting_count"
                return jsonify({"reply": "Great! How many tickets would you like to reserve?"})
        return jsonify({"reply": f"Please choose a valid time for {show}: {', '.join(shows[show]['times'].keys())}"})

    # Step: ticket count
    if user_states["current_step"] == "awaiting_count":
        try:
            count = int(user_message)
            if count <= 0 or count > 10:
                return jsonify({"reply": "Please enter a number between 1 and 10."})
            user_states["ticket_count"] = count
            user_states["current_step"] = "confirm"
            return jsonify({"reply": f"Confirming: {count} tickets for {user_states['selected_show']} at {user_states['selected_time']}? (yes/no)"})
        except ValueError:
            return jsonify({"reply": "Please enter a number (e.g., 2)."})

    # Step: confirm booking
    if user_states["current_step"] == "confirm":
        if "yes" in user_message:
            show = user_states["selected_show"]
            time = user_states["selected_time"]
            count = user_states["ticket_count"]

            available = shows[show]["times"].get(time, 0)
            if count > available:
                user_states["current_step"] = None
                return jsonify({"reply": f" Sorry, only {available} seats are available for {show} at {time}."})

            shows[show]["times"][time] -= count
            user_states["current_step"] = None
            return jsonify({"reply": f" {count} ticket(s) booked for {show} at {time}. Enjoy the show! "})

        elif "no" in user_message:
            user_states["current_step"] = None
            return jsonify({"reply": "Booking cancelled. Let me know if you'd like to book again."})
        else:
            return jsonify({"reply": "Please answer with 'yes' or 'no'."})

    # Cancel
    if "cancel" in user_message:
        user_states["current_step"] = None
        return jsonify({"reply": "Your booking has been cancelled."})

    # Help
    if "help" in user_message or "support" in user_message:
        return jsonify({"reply": "You can ask about shows, book or cancel tickets, or request help!"})

    return jsonify({"reply": "Sorry, I didn’t quite understand. You can ask about shows, book or cancel tickets, or request help."})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
