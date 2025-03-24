import React from 'react';
import { useAuth } from '../hooks/useAuth';

export default function LoginButton() {
  const { login } = useAuth();
  return <button onClick={login}>Login</button>;
}
