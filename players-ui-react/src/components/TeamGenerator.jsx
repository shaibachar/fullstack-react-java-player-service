import React, { useState } from 'react';
import { generateTeam } from '../utils/DataFetcher';

export default function TeamGenerator() {
  const [form, setForm] = useState({ seed_id: '', team_size: 5 });
  const [team, setTeam] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const result = await generateTeam({ seed_id: form.seed_id, team_size: Number(form.team_size) });
      setTeam(result);
    } catch (err) {
      setError('Failed to generate team');
    }
    setLoading(false);
  };

  return (
    <div>
      <h2>Team Generator (a4a_model)</h2>
      <form onSubmit={handleSubmit}>
        <input name="seed_id" placeholder="Seed Player ID" value={form.seed_id} onChange={handleChange} />
        <input name="team_size" type="number" min="1" max="20" value={form.team_size} onChange={handleChange} />
        <button type="submit" disabled={loading}>Generate Team</button>
      </form>
      {error && <div style={{color:'red'}}>{error}</div>}
      {team && (
        <div>
          <h3>Team Members</h3>
          <ul>
            {team.member_ids.map(id => <li key={id}>{id}</li>)}
          </ul>
        </div>
      )}
    </div>
  );
}
