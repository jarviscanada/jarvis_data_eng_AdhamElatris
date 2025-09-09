import React from 'react';
import './TraderItem.scss';

function TraderItem({ trader, onDelete }) {
  return (
    <div className="trader-item">
      <span>{trader.name}</span>
      <button className="delete-btn" onClick={() => onDelete(trader.id)}>Delete</button>
    </div>
  );
}

export default TraderItem;
