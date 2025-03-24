export const authConfig = {
  clientId: 'frontend-client',
  authorizationEndpoint: import.meta.env.VITE_AUTHORIZATION_ENDPOINT,
  tokenEndpoint: import.meta.env.VITE_TOKEN_ENDPOINT,
  redirectUri: import.meta.env.VITE_REDIRECT_URI,
  scope: ['openid', 'profile', 'read', 'write'],
};
