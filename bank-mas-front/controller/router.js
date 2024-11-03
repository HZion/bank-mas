
const express = require('express');
const router = express.Router();
const path = require('path');

router.get('/health', (req, res) => {
  res.status(200).send('OK');  // 헬스 체크용 응답
});

router.get('/', (req, res) => {
 
  res.sendFile(path.join(__dirname, '../src/view/login.html'));
});

router.get('/home', (req, res) => {

  res.sendFile(path.join(__dirname, '../src/view/home.html'));
});
router.get('/login', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/login.html'));
});
router.get('/signup', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/signup.html'));
});

router.get('/all-functions', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/all-functions.html'));
});

router.get('/account', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/account.html'));
});
router.get('/allAccount', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/allAccount.html'));
});
router.post('/allAccount', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/allAccount.html'));
});
router.get('/checkAccount', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/checkAccount.html'));
});
router.get('/detailAccount', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/detailAccount.html'));
});
router.get('/resultTransaction', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/resultTransaction.html'));
});
router.get('/transaction', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/transaction.html'));
});

router.get('/trading', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/trading.html'));
});

router.get('/trading2', (req, res) => {
  res.sendFile(path.join(__dirname, '../src/view/trading2.html'));
});

module.exports = router; 