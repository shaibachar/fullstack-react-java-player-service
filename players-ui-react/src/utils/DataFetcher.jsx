const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export async function fetchPlayers() {
    return fetch(`${API_BASE_URL}/v1/players`)
        .then(response => response.json())
        .catch(error => {
            console.log('oops there was an error', error);
            return { players: [] };
        });
}

export async function fetchPlayerById(playerId) {
    return fetch(`${API_BASE_URL}/v1/players/${playerId}`)
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
    return fetch(`${API_BASE_URL}/v1/players/birthCountry/${countryCode}`)
        .then(response => {
            if (!response.ok) throw new Error('No players found for country');
            return response.json();
        })
        .catch(error => {
            console.log('Error fetching players by country:', error);
            return { players: [] };
        });
}