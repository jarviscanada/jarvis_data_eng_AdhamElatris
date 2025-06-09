import React from 'react';
import TraderItem from '../TraderItem/TraderItem';
import './TraderList.scss';

function TraderList({ traders, onDelete }) {
  return (
    <div className="trader-list">
      {traders.map(trader => (
        <TraderItem key={trader.id} trader={trader} onDelete={onDelete} />
      ))}
    </div>
  );
}

export default TraderList;
