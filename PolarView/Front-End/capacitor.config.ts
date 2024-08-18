import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.PolarView.app',
  appName: 'PolarView',
  webDir: 'dist/polar-view',
  server: {
    androidScheme: 'https'
  }  
};

export default config;
