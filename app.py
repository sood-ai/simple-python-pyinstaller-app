# standard imports
import os
import shutil
import time
import json
import warnings

from flask import Flask, jsonify, json, request

app = Flask(__name__)


@app.route("/Hello")
def route_hello():
    return "Hello World"


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8053)
