export interface Trader {
  key: string;            // your unique string key
  id: number;
  firstName: string;
  lastName: string;
  dob: string;
  country: string;
  email: string;
  amount: number;
  actions: string;        // since you store HTML string for buttons here
}
