
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
const API_VERSION = process.env.REACT_APP_API_VERSION || 'v1';

export async function chatPlayersCsv(question) {
    return fetch(`${API_BASE_URL}/${API_VERSION}/chat/players-csv`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(question),
    })
        .then(response => response.text())
        .catch(error => {
            console.log('Error chatting with players.csv:', error);
            return '';
        });
}

export async function fetchPlayers() {
    return fetch(`${API_BASE_URL}/${API_VERSION}/players`)
        .then(response => response.json())
        .catch(error => {
            console.log('oops there was an error', error);
            return { players: [] };
        });
}

export async function fetchPlayerById(playerId) {
    return fetch(`${API_BASE_URL}/${API_VERSION}/players/${playerId}`)
        .then(response => {
            if (!response.ok) throw new Error('Player not found');
            return response.json();
        })
        .catch(error => {
            console.log('Error fetching player by ID:', error);
            return null;
        });
}

export async function fetchPlayersByCountry(countryCode) {
    return fetch(`${API_BASE_URL}/${API_VERSION}/players/birthCountry/${countryCode}`)
        .then(response => {
            if (!response.ok) throw new Error('No players found for country');
            return response.json();
        })
        .catch(error => {
            console.log('Error fetching players by country:', error);
            return { players: [] };
        });
}