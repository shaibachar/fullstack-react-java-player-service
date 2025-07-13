import logo from '../assets/logo.svg';
import '../styling/PlayersMain.css';
import PlayerResults from "./PlayersResults";
import TeamGenerator from "./TeamGenerator";
import React, { useState } from 'react';


function PlayerMain() {
    const [view, setView] = useState('players');
    return (
        <div className="players-main">
            <header className="players-header">
                <img src={logo} className="players-logo" alt="logo" />
                <p>Hello Players</p>
                <nav>
                    <button onClick={() => setView('players')}>Players Results</button>
                    <button onClick={() => setView('team')}>Team Generator</button>
                </nav>
            </header>
            {view === 'players' ? <PlayerResults/> : <TeamGenerator/>}
        </div>
    );
}

export default PlayerMain;
