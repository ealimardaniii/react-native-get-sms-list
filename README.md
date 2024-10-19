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

Without filters:

const result = await readSMS();
```

Or with optionals filters:

```js
const filters = {
  type: 'inbox', // 'inbox' (default) | 'sent' | 'draft' | 'outbox' | 'failed' | 'queued',
  id: '1', // sms id,
  address: 'Google', // sms address like a phone number,
  orderBy: 'date ASC', // 'date asc' | 'date desc' | ...
  minDate: '1729080977971', // string timestamp
  maxDate: '1729080977971', // string timestamp
  limit: 10, // return max 10 sms,
};
const result = await readSMS(filters);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
