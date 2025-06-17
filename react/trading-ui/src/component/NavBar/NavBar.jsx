import React from 'react'
import './NavBar.scss'
import { NavLink } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAddressBook as dashboardIcon } from '@fortawesome/free-solid-svg-icons';
import { faMoneyBill as quoteIcon } from '@fortawesome/free-solid-svg-icons';


function NavBar() {
  return (
    <nav className="page-navigation">
		    <NavLink to="/" className="page-navigation-header"></NavLink>    
		    <NavLink to="/dashboard" className="page-navigation-item">
		        <FontAwesomeIcon icon={ dashboardIcon } />
		    </NavLink>    
        <NavLink to="/quotes" className="page-navigation-item">
    <FontAwesomeIcon icon={quoteIcon} />
</NavLink>

    </nav>
  )
}

export default NavBar