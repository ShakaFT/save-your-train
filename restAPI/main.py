"""
This module contains main endpoints of default service.
"""
from flask import Flask, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)


@app.get("/")
def default():
    """
    main endpoint.
    """
    return jsonify(success=True)


if __name__ == "__main__":
    app.run(host="localhost", port=8080, debug=True)
