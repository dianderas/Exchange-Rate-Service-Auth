import { useEffect, useState } from 'react';
import LogoutButton from '../components/LogoutButton';
import apiClient from '../config/apiCliente';
import './Dashboard.css';

export default function Dashboard() {
  const [currentExchangeRate, setCurrentExchangeRate] = useState(null);
  const [amount, setAmount] = useState('');
  const [transactionType, setTransactionType] = useState('BUY'); // Por defecto en "BUY"
  const [transactionResult, setTransactionResult] = useState(null);
  const [loadingTransaction, setLoadingTransaction] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchExchangeRate = async () => {
      try {
        const response = await apiClient.get('/exchange-rates/USD/PEN');
        setCurrentExchangeRate(response.data);
      } catch (error) {
        console.error('Error fetching exchange rate:', error);
      }
    };
    fetchExchangeRate();
  }, []);

  const handleTransaction = async () => {
    if (!amount || isNaN(amount) || amount <= 0) {
      setError('Ingrese un monto válido.');
      return;
    }
    setError(null);
    setLoadingTransaction(true);

    try {
      const response = await apiClient.post('/transactions', {
        type: transactionType,
        amount,
        sourceCurrency: 'USD',
        targetCurrency: 'PEN',
      });

      setTransactionResult(response.data);
    } catch (error) {
      console.error('Error creating transaction:', error);
      setError('Error al procesar la transacción.');
    } finally {
      setLoadingTransaction(false);
    }
  };

  return (
    <div className="dashboard">
      <h1>Casa de Cambio</h1>

      {currentExchangeRate ? (
        <div className="exchange-rate">
          <p>
            Tasa de cambio actual: {currentExchangeRate.sourceCurrency}/
            {currentExchangeRate.targetCurrency}
          </p>
          <table>
            <thead>
              <tr>
                <th>Compra</th>
                <th>Venta</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{currentExchangeRate.fxRateBuy}</td>
                <td>{currentExchangeRate.fxRateSell}</td>
              </tr>
            </tbody>
          </table>
        </div>
      ) : (
        <p>Cargando tasas de cambio...</p>
      )}

      <hr />

      <h2>Realizar Transacción</h2>
      <div className="transaction-form">
        <label>
          Monto en USD:&nbsp;
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Ingrese el monto"
          />
        </label>

        <div className="transaction-buttons">
          <button
            className={transactionType === 'BUY' ? 'active' : ''}
            onClick={() => setTransactionType('BUY')}
          >
            Comprar
          </button>
          <button
            className={transactionType === 'SELL' ? 'active' : ''}
            onClick={() => setTransactionType('SELL')}
          >
            Vender
          </button>
        </div>

        <button onClick={handleTransaction} disabled={loadingTransaction}>
          {loadingTransaction ? 'Procesando...' : `Ejecutar ${transactionType}`}
        </button>

        {error && <p className="error">{error}</p>}

        {transactionResult && (
          <div className="transaction-result">
            <h3>Resultado de la Transacción</h3>
            <p>
              Monto recibido: {transactionResult.amountTarget}{' '}
              {transactionResult.targetCurrency}
            </p>
            <p>Tipo: {transactionResult.type}</p>
          </div>
        )}
      </div>

      <div className="logout-button">
        <LogoutButton />
      </div>
    </div>
  );
}
