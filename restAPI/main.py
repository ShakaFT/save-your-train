"""
This module contains main endpoints of default service.
"""
from time import time

from flask import Flask, jsonify, request
from flask_cors import CORS

import constants
import utils

app = Flask(__name__)
CORS(app)

db = utils.initialize_database()


@app.get("/")
def default():
    """
    main endpoint.
    """
    return jsonify(success=True)


@app.post("/account/sign_in")
def sign_in():
    """
    This endpoint check if user is already registered in database.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        password = payload["password"]
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    user_doc = db.collection(constants.COLLECTION_USER).document(email).get()

    if not user_doc.exists:
        return jsonify(user_sign_in=False, reason="email unknown")
    if password != user_doc.to_dict()["password"]:
        return jsonify(user_sign_in=False, reason="password incorrect")

    return jsonify(user_sign_in=True)


@app.post("/account/add")
def add_account():
    """
    This endpoint add a new account in database.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        password = payload["password"]
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    user_ref = db.collection(constants.COLLECTION_USER).document(email)

    if user_ref.get().exists:
        return jsonify(error="user already exists"), 400

    user_ref.set({"password": password})
    return jsonify()


@app.post("/exercise/add")
def add_exercise():
    """
    This endpoint adds exercise to database.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        exercise = payload["exercise"]
        exercise["name"] # pylint: disable=pointless-statement
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    if not db.collection(constants.COLLECTION_USER).document(email).get().exists:
        return jsonify(error="email unknown"), 400

    exercise_ref = db.collection(constants.COLLECTION_EXERCISES).document(email)
    exercises_list = (exercise_ref.get().to_dict()["exercises"] if exercise_ref.get().exists else [])
    exercises_list.append(exercise)
    exercises_list.sort(key=lambda x: x["name"])

    exercise_ref.set({"exercises":exercises_list})
    return jsonify(exercises=exercises_list)


@app.post("/history/add")
def add_history():
    """
    This endpoint adds exercise to history.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        exercise = payload["exercise"]
        history_data = {
            "exercise": exercise,
            "date_ms": time() # pylint: disable=no-member
        }
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    if not db.collection(constants.COLLECTION_USER).document(email).get().exists:
        return jsonify(error="email unknown"), 400

    history_ref = db.collection(constants.COLLECTION_HISTORY).document(email)
    history_list = (history_ref.get().to_dict()["history"] if history_ref.get().exists else [])
    history_list.insert(0, history_data)


    history_ref.set({"history": history_list})
    return jsonify(history=history_list)


@app.post("/start")
def start():
    """
    This endpoint returns utils data when app is launched.
    """
    payload = request.get_json(force=True)
    email = payload.get("email")

    if email is None:
        return jsonify(error="missing email"), 400

    # pylint: disable=no-member
    exercises = db.collection(constants.COLLECTION_EXERCISES).document(email).get().to_dict()
    history = db.collection(constants.COLLECTION_HISTORY).document(email).get().to_dict()

    return jsonify({
        "exercises": exercises["exercises"] if exercises else {},
        "history": history["history"] if history else {}
    })


if __name__ == "__main__":
    app.run(host="localhost", port=8080, debug=True)
