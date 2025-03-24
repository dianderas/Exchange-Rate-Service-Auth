import React from 'react';
import { useAuth } from '../hooks/useAuth';

export default function LogoutButton() {
  const { logout } = useAuth();
  return <button onClick={logout}>Logout</button>;
}
