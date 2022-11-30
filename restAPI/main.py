"""
This module contains main endpoints of default service.
"""
from firebase_admin import firestore
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
    This endpoint checks if user is already registered in database.
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

    # get data
    exercises = db.collection(constants.COLLECTION_EXERCISES).document(email).get().to_dict()
    history = db.collection(constants.COLLECTION_HISTORY).document(email).get().to_dict()

    # check if exercises not already registered
    if not exercises:
        exercises = {}
    if not history:
        history = {}

    return jsonify(
        userSignIn = True,
        exercises = [{"exerciseName": k} | v for k, v in exercises.items()],
        history = [{"dateMs": float(k)} | v for k, v in history.items()]
    )


@app.post("/account/add")
def add_account():
    """
    This endpoint adds a new account in database.
    """
    payload = request.get_json(force=True)

    try:
        email = payload.pop("email")
        # pylint: disable=pointless-statement
        account_data = {
            "firstName": payload["firstName"],
            "lastName": payload["lastName"],
            "password": payload["password"]
        }
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    user_ref = db.collection(constants.COLLECTION_USER).document(email)

    if user_ref.get().exists:
        return jsonify(error="user already exists"), 400

    user_ref.set(account_data)
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
        exercise_name = exercise.pop("exerciseName")
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    if not db.collection(constants.COLLECTION_USER).document(email).get().exists:
        return jsonify(error="email unknown"), 400

    db.collection(constants.COLLECTION_EXERCISES).document(email).set({exercise_name: exercise}, merge=True)
    return jsonify()


@app.post("/exercise/delete")
def delete_exercise():
    """
    This endpoint deletes exercise from database.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        exercise_name = payload["exerciseName"]
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    if not db.collection(constants.COLLECTION_USER).document(email).get().exists:
        return jsonify(error="email unknown"), 400

    db.collection(constants.COLLECTION_EXERCISES).document(email).set({
        exercise_name: firestore.DELETE_FIELD}, merge=True)
    return jsonify()


@app.post("/history/add")
def add_history():
    """
    This endpoint adds an exercise to history.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        exercise = payload["exercise"]
        timestamp = str(exercise.pop("dateMs"))
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    if not db.collection(constants.COLLECTION_USER).document(email).get().exists:
        return jsonify(error="email unknown"), 400

    db.collection(constants.COLLECTION_HISTORY).document(email).set({timestamp: exercise}, merge=True)
    return jsonify()


@app.post("/history/delete")
def delete_history():
    """
    This endpoint deletes an exercise from history from database.
    """
    payload = request.get_json(force=True)

    try:
        email = payload["email"]
        timestamp = str(payload["dateMs"])
    except KeyError as e:
        return jsonify(error=f"missing {str(e)}"), 400

    # pylint: disable=no-member
    if not db.collection(constants.COLLECTION_USER).document(email).get().exists:
        return jsonify(error="email unknown"), 400

    db.collection(constants.COLLECTION_HISTORY).document(email).set({
        timestamp: firestore.DELETE_FIELD}, merge=True)
    return jsonify()


if __name__ == "__main__":
    app.run(host="localhost", port=8080, debug=True)
