import yfinance as yf
import certifi

# 获取股票数据
# symbol = "600519.SS"
symbol = "000001.SZ"
start_date = "2022-01-01"
end_date = "2023-01-01"

data = yf.download(symbol, start=start_date, end=end_date, auto_adjust=True, requests_kwargs={'verify': certifi.where()})
print(data.head())
