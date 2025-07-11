import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

import { validateId, validateCountryCode, sanitizeInput } from "../utils";
import {
  fetchPlayers,
  fetchPlayerById,
  fetchPlayersByCountry,
} from "../utils/DataFetcher";

import { chatPlayersCsv } from "../utils/DataFetcher";

function PlayerResults() {
  const [players, setPlayers] = useState([]);
  const [playerIdInput, setPlayerIdInput] = useState("");
  const [countryCodeInput, setCountryCodeInput] = useState("");
  const [sortField, setSortField] = useState("playerId");
  const [sortOrder, setSortOrder] = useState("asc");
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [lastFetchType, setLastFetchType] = useState("all"); // 'all', 'id', 'country'
  const [lastFetchParam, setLastFetchParam] = useState("");
  const [csvChatInput, setCsvChatInput] = useState("");
  const [csvChatResponse, setCsvChatResponse] = useState("");
  // Chat with players.csv
  const handleCsvChat = async () => {
    if (csvChatInput.trim().length === 0) {
      alert("Please enter a question about the players.csv file.");
      return;
    }
    const response = await chatPlayersCsv(csvChatInput);
    setCsvChatResponse(response);
  };

  useEffect(() => {
    // Only fetch all players on initial mount or when lastFetchType is 'all'
    if (lastFetchType === "all") {
      fetchPlayers(page, size).then((data) => {
        setPlayers(data.players || []);
      });
    } else if (lastFetchType === "country") {
      fetchPlayersByCountry(lastFetchParam, page, size).then((data) => {
        setPlayers(data.players || []);
      });
    } else if (lastFetchType === "id") {
      fetchPlayerById(lastFetchParam).then((data) => {
        setPlayers(data ? [data] : []);
      });
    }
  }, [page, size, lastFetchType, lastFetchParam]);

  // Search for a player by ID
  const handleSearchById = async () => {
    if (validateId(playerIdInput)) {
      setLastFetchType("id");
      setLastFetchParam(playerIdInput);
      setPage(0); // reset to first page for new search
      const data = await fetchPlayerById(playerIdInput);
      if (data) {
        setPlayers([data]);
      } else {
        setPlayers([]);
      }
    } else {
      alert("Invalid player ID");
    }
  };

  // Search for players by country code
  const handleSearchByCountry = async () => {
    if (validateCountryCode(countryCodeInput)) {
      setLastFetchType("country");
      setLastFetchParam(countryCodeInput);
      setPage(0); // reset to first page for new search
      const data = await fetchPlayersByCountry(countryCodeInput, page, size);
      if (data) {
        setPlayers(data.players || []);
      } else {
        setPlayers([]);
      }
    } else {
      alert("Invalid country code");
    }
  };

  // Pagination controls
  const handleNextPage = () => {
    setPage((prevPage) => prevPage + 1);
  };

  const handlePrevPage = () => {
    setPage((prevPage) => (prevPage > 0 ? prevPage - 1 : 0));
  };

  const handleFirstPage = () => {
    setPage(0);
  };

  // Optionally, you could add a max page if you know total count
  // For now, just allow manual entry and next/prev/first/last
  const handleLastPage = () => {
    // This would require knowing the total number of pages
    // For now, just a placeholder
    alert("Last page functionality requires total count from backend.");
  };

  // Sorting logic
  const sortedPlayers = [...players].sort((a, b) => {
    if (!a[sortField] || !b[sortField]) return 0;
    if (a[sortField] < b[sortField]) return sortOrder === "asc" ? -1 : 1;
    if (a[sortField] > b[sortField]) return sortOrder === "asc" ? 1 : -1;
    return 0;
  });

  const handleSort = (field) => {
    if (sortField === field) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortOrder("asc");
    }
  };

  return (
    <div className="container mt-4">
      <div className="row mb-3">
        <div className="col-md-12">
          <label className="form-label">Ask a question about players.csv:</label>
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              value={csvChatInput}
              onChange={(e) => setCsvChatInput(e.target.value)}
              placeholder="e.g. Who has the most home runs?"
            />
            <button className="btn btn-success" onClick={handleCsvChat}>
              Submit
            </button>
          </div>
          {csvChatResponse && (
            <div className="alert alert-info mt-2">
              <strong>Response:</strong> {csvChatResponse}
            </div>
          )}
        </div>
      </div>
      <div className="row mb-3">
        <div className="col-md-4">
          <label className="form-label">Player id:</label>
          <input
            type="text"
            className="form-control"
            value={playerIdInput}
            onChange={(e) => setPlayerIdInput(e.target.value)}
            placeholder="Enter player ID"
          />
          <button className="btn btn-primary mt-2" onClick={handleSearchById}>
            Submit
          </button>
        </div>
        <div className="col-md-4">
          <label className="form-label">Player Country Code:</label>
          <input
            type="text"
            className="form-control"
            value={countryCodeInput}
            onChange={(e) => setCountryCodeInput(e.target.value)}
            placeholder="Enter country code"
          />
          <button
            className="btn btn-primary mt-2"
            onClick={handleSearchByCountry}
          >
            Submit
          </button>
        </div>
      </div>
      <div className="table-responsive">
        <table className="table table-striped table-bordered">
          <thead>
            <tr>
              <th
                style={{ cursor: "pointer" }}
                onClick={() => handleSort("playerId")}
              >
                Player ID{" "}
                {sortField === "playerId"
                  ? sortOrder === "asc"
                    ? "▲"
                    : "▼"
                  : ""}
              </th>
              <th
                style={{ cursor: "pointer" }}
                onClick={() => handleSort("birthCountry")}
              >
                Birth Country{" "}
                {sortField === "birthCountry"
                  ? sortOrder === "asc"
                    ? "▲"
                    : "▼"
                  : ""}
              </th>
            </tr>
          </thead>
          <tbody>
            {sortedPlayers.map((player) => (
              <tr key={player.playerId}>
                <td>{player.playerId}</td>
                <td>{player.birthCountry}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="row mb-3">
        <div className="col-md-2">
          <label className="form-label">Page Number:</label>
          <input
            type="number"
            className="form-control"
            value={page}
            min={0}
            onChange={(e) => setPage(Number(e.target.value))}
          />
        </div>
        <div className="col-md-2">
          <label className="form-label">Page Size:</label>
          <input
            type="number"
            className="form-control"
            value={size}
            min={1}
            onChange={(e) => setSize(Number(e.target.value))}
          />
        </div>
        <div className="col-md-8 text-end">
          <button className="btn btn-outline-secondary me-2" onClick={handleFirstPage}>
            First Page
          </button>
          <button className="btn btn-outline-secondary me-2" onClick={handlePrevPage}>
            Previous Page
          </button>
          <button className="btn btn-secondary me-2" onClick={handleNextPage}>
            Next Page
          </button>
          <button className="btn btn-outline-secondary" onClick={handleLastPage}>
            Last Page
          </button>
        </div>
      </div>
    </div>
  );
}

export default PlayerResults;
