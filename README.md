# stockdata-challenge

1. Input CSV:
http://www.google.com/finance/historical?cid=700145&startdate=Jan+1%2C+1990&enddate=Jul+27%2C+2017&output=csv

2. Output:
JSON array of: {'date':'MM-dd-YYYY','close':<amount>}
where 'date' is the last day of a month and 'close' is the closing price of said date.
