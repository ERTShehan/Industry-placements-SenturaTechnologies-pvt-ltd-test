import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [countries, setCountries] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCountry, setSelectedCountry] = useState(null);

  useEffect(() => {
    fetchCountries();
  }, []);

  const fetchCountries = async (query = '') => {
    try {
      const url = query 
        ? `http://localhost:8080/api/countries?search=${query}` 
        : `http://localhost:8080/api/countries`;
      const response = await fetch(url);
      const data = await response.json();
      setCountries(data);
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };

  const handleSearch = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    fetchCountries(value);
  };

  return (
    <div className="App">
      <h1>Countries Dashboard</h1>
      
      {/* Search Input */}
      <input 
        type="text" 
        placeholder="Search countries..." 
        value={searchTerm}
        onChange={handleSearch}
        className="search-input"
      />
      
      {/* Countries Table */}
      <table className="countries-table">
        <thead>
          <tr>
            <th>Flag</th>
            <th>Name</th>
            <th>Capital</th>
            <th>Region</th>
            <th>Population</th>
          </tr>
        </thead>
        <tbody>
          {countries.map((country, index) => (
            <tr key={index} onClick={() => setSelectedCountry(country)} className="table-row">
              <td><img src={country.flag} alt={`${country.name} flag`} className="flag-img"/></td>
              <td>{country.name}</td>
              <td>{country.capital}</td>
              <td>{country.region}</td>
              <td>{country.population.toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Country Details Modal / Popup */}
      {selectedCountry && (
        <div className="modal-overlay" onClick={() => setSelectedCountry(null)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <button className="close-btn" onClick={() => setSelectedCountry(null)}>&times;</button>
            <h2>{selectedCountry.name}</h2>
            <img src={selectedCountry.flag} alt="Flag" className="modal-flag" />
            <p><strong>Capital:</strong> {selectedCountry.capital}</p>
            <p><strong>Region:</strong> {selectedCountry.region}</p>
            <p><strong>Population:</strong> {selectedCountry.population.toLocaleString()}</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;