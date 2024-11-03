// router/userRouter.js
const express = require('express');
const router = express.Router();

// 유저 목록 가져오기
router.get('/', (req, res) => {
  res.send('유저 목록');
});

module.exports = router;
