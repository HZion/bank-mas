require('dotenv').config();
const { Eureka } = require('eureka-js-client');

const HOST = process.env.HOST || 'localhost';
const PORT = process.env.PORT || 38081;
const EUREKA_HOST = process.env.EUREKA_HOST || 'localhost';
const EUREKA_PORT = process.env.EUREKA_PORT || 38761;

const client = new Eureka({
  instance: {
    app: 'javaMsaFront',
    instanceId: `javaMsaFront:${HOST}:${PORT}`,
    hostName: HOST,
    ipAddr: HOST,
    port: {
      '$': parseInt(PORT),
      '@enabled': true,
    },
    vipAddress: 'javaMsaFront',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn',
    },
    statusPageUrl: `http://${HOST}:${PORT}/actuator/info`,
    healthCheckUrl: `http://${HOST}:${PORT}/actuator/health`,
    homePageUrl: `http://${HOST}:${PORT}`,
    preferIpAddress: true,
  },
  eureka: {
    host: EUREKA_HOST,
    port: EUREKA_PORT,
    servicePath: '/eureka',
    maxRetries: 10,
    requestRetryDelay: 2000,
    registryFetchInterval: 5000,
  },
});


module.exports = client;
