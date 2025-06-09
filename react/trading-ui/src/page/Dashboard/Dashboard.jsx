import React, { useEffect, useState } from 'react';
import './Dashboard.scss';
import NavBar from '../../component/NavBar/NavBar';
import TraderList from '../../component/TraderList/TraderList';
import { tradersUrl, deleteTraderUrl } from '../../util/constants';

function Dashboard() {
  const [traders, setTraders] = useState([]);

  useEffect(() => {
    fetch(tradersUrl)
      .then(res => res.json())
      .then(data => setTraders(data))
      .catch(err => console.error('Failed to fetch traders', err));
  }, []);

  const handleDeleteTrader = (id) => {
    const url = deleteTraderUrl.replace('traderId', id);
    fetch(url, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          setTraders(prev => prev.filter(trader => trader.id !== id));
        } else {
          console.error('Failed to delete');
        }
      })
      .catch(err => console.error('Delete error', err));
  };

  return (
    <div className="dashboard">
      <NavBar />
      <div className="dashboard-content">
        <h2>Traders</h2>
        <TraderList traders={traders} onDelete={handleDeleteTrader} />
      </div>
    </div>
  );
}

export default Dashboard;
