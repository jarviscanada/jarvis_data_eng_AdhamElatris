import React, { useState, useEffect } from 'react';
import axios from 'axios';
import NavBar from '../../component/NavBar/NavBar';
import { Table, Spin } from 'antd';
import './QuotePage.scss';

const API_KEY = 'BKBZUGNUCFSX9D7X'; // Replace with your actual key
const TICKERS = ['AAPL', 'TSLA', 'GOOGL'];

function QuotePage() {
  const [quotes, setQuotes] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchQuote = async (symbol) => {
    try {
      const res = await axios.get('https://www.alphavantage.co/query', {
        params: {
          function: 'TIME_SERIES_DAILY',
          symbol,
          apikey: API_KEY,
        },
      });

      const timeSeries = res.data['Time Series (Daily)'];
      if (!timeSeries) return null;

      const latestDate = Object.keys(timeSeries)[0];
      const latestData = timeSeries[latestDate];

      return {
        key: symbol,
        ticker: symbol,
        lastPrice: parseFloat(latestData['4. close']),
        bidPrice: parseFloat(latestData['1. open']),
        bidSize: parseFloat(latestData['2. high']), // simulated
        askPrice: parseFloat(latestData['3. low']),
        askSize: Math.floor(Math.random() * 100000000), // simulated
      };
    } catch (err) {
      console.error(`Error fetching ${symbol}:`, err);
      return null;
    }
  };

  useEffect(() => {
    const loadQuotes = async () => {
      setLoading(true);
      const results = await Promise.all(TICKERS.map(fetchQuote));
      setQuotes(results.filter(Boolean));
      setLoading(false);
    };

    loadQuotes();
  }, []);

  const columns = [
    { title: 'Ticker', dataIndex: 'ticker', key: 'ticker' },
    { title: 'Last Price', dataIndex: 'lastPrice', key: 'lastPrice' },
    { title: 'Bid Price', dataIndex: 'bidPrice', key: 'bidPrice' },
    { title: 'Bid Size', dataIndex: 'bidSize', key: 'bidSize' },
    { title: 'Ask Price', dataIndex: 'askPrice', key: 'askPrice' },
    { title: 'Ask Size', dataIndex: 'askSize', key: 'askSize' },
  ];

  return (
    <div className="quote-page">
      <NavBar />
      <div className="quote-page-content">
        <div className="title">Quotes</div>

        {loading ? (
          <div className="loading">
            <Spin size="large" />
          </div>
        ) : (
          <Table
            dataSource={quotes}
            columns={columns}
            pagination={false}
            bordered
          />
        )}
      </div>
    </div>
  );
}

export default QuotePage;
