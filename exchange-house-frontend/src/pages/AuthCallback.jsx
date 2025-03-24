import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { authConfig } from '../config/authConfig';

export default function Callback() {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');

    if (!code) {
      console.error('No authorization code found.');
      navigate('/');
      return;
    }

    exchangeCodeForToken(code);
  }, []);

  async function exchangeCodeForToken(code) {
    const codeVerifier = localStorage.getItem('pkce_code_verifier');

    const params = new URLSearchParams();
    params.append('grant_type', 'authorization_code');
    params.append('code', code);
    params.append('redirect_uri', authConfig.redirectUri);
    params.append('client_id', authConfig.clientId);
    params.append('code_verifier', codeVerifier);

    try {
      const response = await axios.post(authConfig.tokenEndpoint, params, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      });

      localStorage.setItem('access_token', response.data.access_token);
      navigate('/dashboard');
    } catch (error) {
      console.error('Error exchanging code for token', error);
      navigate('/');
    }
  }

  return <p>Processing login...</p>;
}
