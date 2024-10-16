# react-native-get-sms-list

Read android sms list

## Installation

```sh
yarn add react-native-get-sms-list
```

```sh
npm i react-native-get-sms-list
```

## Android Permissions

Add permissions to your android/app/src/main/AndroidManifest.xml file:

```
  <uses-permission android:name="android.permission.READ_SMS" />
```

## Usage

```js
import { readSMS } from 'react-native-get-sms-list';

// ...

const result = await readSMS();
or;
const filters = {
  orderBy: 'date ASC',
  minDate: '1729080977971', // string timestamp
  // maxDate: '1729080977971', // string timestamp
};
const result = await readSMS(filters);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
