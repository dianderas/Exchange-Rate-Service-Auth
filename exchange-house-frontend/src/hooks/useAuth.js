import { authConfig } from '../config/authConfig';

function generateRandomString(length) {
  const array = new Uint8Array(length);
  window.crypto.getRandomValues(array);
  return btoa(String.fromCharCode(...array))
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=+$/, '');
}

async function generateCodeChallenge(codeVerifier) {
  const encoder = new TextEncoder();
  const data = encoder.encode(codeVerifier);
  const digest = await crypto.subtle.digest('SHA-256', data);
  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=+$/, '');
}

export function useAuth() {
  function login() {
    const codeVerifier = generateRandomString(128);
    generateCodeChallenge(codeVerifier).then((codeChallenge) => {
      localStorage.setItem('pkce_code_verifier', codeVerifier);

      const authUrl = `${
        authConfig.authorizationEndpoint
      }?response_type=code&client_id=${
        authConfig.clientId
      }&redirect_uri=${encodeURIComponent(
        authConfig.redirectUri
      )}&scope=${encodeURIComponent(
        authConfig.scope.join(' ')
      )}&code_challenge=${codeChallenge}&code_challenge_method=S256`;

      window.location.href = authUrl;
    });
  }

  function logout() {
    localStorage.removeItem('access_token');
    window.location.href = '/';
  }

  return { login, logout };
}
