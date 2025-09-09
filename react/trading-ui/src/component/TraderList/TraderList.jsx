import React from 'react';
import { Table } from 'antd';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrashAlt as deleteIcon } from '@fortawesome/free-solid-svg-icons';
import 'antd/dist/antd.css';
import './TraderList.scss';

function TraderList(props) {
  const columns = [
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName',
      sorter: (a, b) => a.firstName.localeCompare(b.firstName),
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName',
      sorter: (a, b) => a.lastName.localeCompare(b.lastName),
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Date of Birth',
      dataIndex: 'dob',
      key: 'dob',
      sorter: (a, b) => new Date(a.dob) - new Date(b.dob),
    },
    {
      title: 'Country',
      dataIndex: 'country',
      key: 'country',
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_, record) => (
        <div className="trader-delete-icon">
          <FontAwesomeIcon
            icon={deleteIcon}
            style={{ cursor: 'pointer', color: 'red' }}
            onClick={() => props.onTraderDeleteClick(record.id)}
            title="Delete Trader"
          />
        </div>
      ),
    },
  ];

  return (
    <Table
      dataSource={props.traders}
      columns={columns}
      pagination={false}
      rowKey="id"
    />
  );
}

export default TraderList;
