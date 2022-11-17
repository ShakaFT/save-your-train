"""
This method contains utils functions.
"""
import os

from firebase_admin import credentials, firestore, initialize_app

# apikey

def initialize_database():
    """
    This function initializes database.
    """
    path_cred = os.path.join(os.path.dirname(os.path.realpath(__file__)), "keys/save-your-train-a54cca872129.json")
    if os.getenv('GAE_ENV', '').startswith('standard'):
        # Production in the standard environment
        initialize_app()
    else:
        # Local development server
        cred = credentials.Certificate(path_cred)
        initialize_app(cred)
    return firestore.client()
