import React, { useState, useEffect } from 'react';
import NavBar from '../../component/NavBar/NavBar';
import TraderList from '../../component/TraderList/TraderList';
import { Input, DatePicker, Modal, Button, Form } from 'antd';
import 'antd/dist/antd.css';
import './Dashboard.scss';
import TraderListData from '../../component/TraderList/TraderListData.json';

function Dashboard() {
  const [traders, setTraders] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    setTraders(TraderListData);
  }, []);

  const onTraderDelete = (id) => {
    const filtered = traders.filter((t) => t.id !== id);
    setTraders(filtered);
    console.log(`Trader ${id} deleted`);
  };

  const showModal = () => setIsModalVisible(true);

  const handleOk = () => {
    form.validateFields().then(values => {
      const newId = traders.length ? Math.max(...traders.map(t => t.id)) + 1 : 1;
      const newTrader = { id: newId, ...values, dob: values.dob.format('YYYY-MM-DD') };

      setTraders([...traders, newTrader]);
      setIsModalVisible(false);
      form.resetFields();
    }).catch(info => {
      console.log('Validation Failed:', info);
    });
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
  };

  return (
    <div className="dashboard">
      <NavBar />
      <div className="dashboard-content">
        <div className="title">
          Dashboard
          <div className="add-trader-button">
            <Button type="primary" onClick={showModal}>
              Add New Trader
            </Button>
          </div>
        </div>

        <TraderList traders={traders} onTraderDeleteClick={onTraderDelete} />

        <Modal
          title="Add New Trader"
          visible={isModalVisible}
          onOk={handleOk}
          onCancel={handleCancel}
          okText="Submit"
        >
          <Form form={form} layout="vertical" name="add_trader_form">
            <Form.Item
              label="First Name"
              name="firstName"
              rules={[{ required: true, message: 'Please enter first name' }]}
            >
              <Input placeholder="John" />
            </Form.Item>

            <Form.Item
              label="Last Name"
              name="lastName"
              rules={[{ required: true, message: 'Please enter last name' }]}
            >
              <Input placeholder="Doe" />
            </Form.Item>

            <Form.Item
              label="Email"
              name="email"
              rules={[
                { required: true, message: 'Please enter email' },
                { type: 'email', message: 'Please enter a valid email' }
              ]}
            >
              <Input placeholder="john.doe@example.com" />
            </Form.Item>

            <Form.Item
              label="Country"
              name="country"
              rules={[{ required: true, message: 'Please enter country' }]}
            >
              <Input placeholder="USA" />
            </Form.Item>

            <Form.Item
              label="Date of Birth"
              name="dob"
              rules={[{ required: true, message: 'Please select date of birth' }]}
            >
              <DatePicker style={{ width: '100%' }} />
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </div>
  );
}

export default Dashboard;
