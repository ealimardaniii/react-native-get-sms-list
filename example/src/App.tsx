import { useEffect } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { readSMS } from 'react-native-get-sms-list';

export default function App() {
  useEffect(() => {
    readSMS({
      address: 'Google',
      limit: 1,
    })
      .then((result) => console.log('SMS list result:', result))
      .catch((error) => console.error('SMS list error:', error));
  }, []);

  return (
    <View style={styles.container}>
      <Text>Get all SMS list example</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
