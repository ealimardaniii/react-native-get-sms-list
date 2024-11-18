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

**Without any filters:**

```js
import { readSMS } from 'react-native-get-sms-list';

// ...

const result = await readSMS();
```

**Or with optionals filters:**

```js
const filters = {
  type: 'inbox', // 'inbox' (default) | 'sent' | 'draft' | 'outbox' | 'failed' | 'queued',
  id: '1', // specefic sms id,
  address: 'Google', // sms address like a phone number,
  orderBy: 'date ASC', // 'date asc' | 'date desc' | ...
  minDate: '1729080977971', // string timestamp
  maxDate: '1729080977971', // string timestamp
  limit: 10, // return max 10 rows,
  thread_id: '1', // return all sms on thread_id 1
};
const result = await readSMS(filters);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
