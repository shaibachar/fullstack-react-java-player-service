# 🏆 a4a_model: Team Generation & LLM Microservice

This project provides a Python microservice for team generation and LLM-based description generation, built with Flask, Pydantic, and scikit-learn. It exposes RESTful endpoints for generating player teams, collecting feedback, and interacting with a language model.

## Features
- **Team Generation API**: Generate a team of players based on seed player or custom features using a trained nearest-neighbors model.
- **Team Feedback API**: Submit feedback to exclude specific players from future team generations.
- **LLM Description API**: Generate descriptions using a language model (stubbed for extension).
- **LLM Feedback API**: Submit feedback for generated descriptions.

## File Structure
- `server.py` — Main Flask app with API endpoints
- `team_model.joblib` — Trained nearest-neighbors model (scikit-learn)
- `features_db.csv` — Player features database
- `player.csv` — Raw player data
- `train.ipynb` — Jupyter notebook for model training

## API Endpoints

### 1. Team Generation
`POST /team/generate`
- **Request Body:**
  ```json
  {
    "seed_id": "player123",           // Optional: seed player ID
    "features": {                      // Optional: custom features
      "birth_year": 1990,
      "height": 180,
      "weight": 75,
      "bats": "R",                   // "L", "R", or "N"
      "throws": "L"                   // "L", "R", or "N"
    },
    "team_size": 5
  }
  ```
- **Response:**
  ```json
  {
    "seed_id": "player123",
    "prediction_id": "...",
    "team_size": 5,
    "member_ids": ["playerA", "playerB", ...]
  }
  ```

### 2. Team Feedback
`POST /team/feedback`
- **Request Body:**
  ```json
  {
    "seed_id": "player123",
    "member_id": "playerA",
    "feedback": -1    // -1 to exclude, 1 to accept
  }
  ```
- **Response:**
  ```json
  {
    "seed_id": "player123",
    "member_id": "playerA",
    "accepted": false
  }
  ```

### 3. LLM Description Generation
`POST /llm/generate`
- **Request Body:**
  ```json
  {
    "system_prompt": "Describe the team...", // Optional
    "user_prompt": "Generate a summary for team X"
  }
  ```
- **Response:**
  ```json
  {
    "response": "Generated Description"
  }
  ```

### 4. LLM Feedback
`POST /llm/feedback`
- **Request Body:**
  ```json
  {
    "feedback": "Great description!"
  }
  ```
- **Response:**
  ```json
  {
    "message": "Description feedback received"
  }
  ```

## Setup & Usage

### Prerequisites
- Python 3.9+
- pip

### Install dependencies
```bash
pip install -r requirements.txt
```

### Model & Data
- Place `team_model.joblib` and `features_db.csv` in the project directory.
- (Optional) Use `train.ipynb` to retrain or update the model.

### Run the server
```bash
python server.py
```
The service will start on `http://0.0.0.0:5000` by default.

## Development
- API validation is handled by Pydantic and Flask-Pydantic.
- Extend the LLM endpoints to connect to a real language model (e.g., Ollama, OpenAI).
- Use `train.ipynb` for feature engineering and model retraining.

## Example Usage
See the API section above for sample requests and responses.


# Player Service Model

This is a thin model wrapper container based on `Player.csv` data.

To build and run:
```shell
docker build -t a4a_model .
docker run -d -p 5000:5000 a4a_model
```

This will expose port 5000.

To send an inference request to the AI model using a seed from the database:
```shell
$ curl -H "Content-type: application/json" -d '{"seed_id":"abbotji01","team_size":10}' http://127.0.0.1:5000/team/generate
{"seed_id":"abbotji01","team_size":10,"member_ids":["abbotji01","combspa01","maurero01","cummijo01","flemida01","macdobo01","eddych01","morriha02","mcgrifr01","blossgr01"]}
```

To send an inference request to the AI model using a set of features:
```shell
$ curl -H "Content-type: application/json" -d '{"features":{"birth_year":1970, "height":70, "weight":120, "bats":"R", "throws":"L"},"team_size":10}' http://127.0.0.1:5000/team/generate
{"seed_id":null,"prediction_id":"ddedf511-2e68-4ab5-87c5-c77b8d15eb23","team_size":10,"member_ids":["roblevi01","deverra01","goharlu01","albieoz01","barrefr02","urenari01","uriasju01","verdual01","mejiafr01","sierrma01"]}
```
Note that the features are optional, and features that are not provided will be assumed to be the mean values in the training dataset. The unit of weight is pounds. The unit of height is inches. Batting and throwing may be right handed (`R`), left handed (`L`) or no preference (`N`).

To send feedback about the recommendations for a prior seed:
```shell
$ curl -H "Content-type: application/json"  -d '{"seed_id":"abbotji01","member_id":"maurero01","feedback":-1}' http://127.0.0.1:5000/team/feedback 
{"seed_id":"abbotji01","member_id":"maurero01","accepted":true}
```