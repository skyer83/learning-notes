import requests

symbol = "000001.SZ"
startTime = "20251013"
endTime = "20251013"
# endTime = "20250103"
url = f"https://api.mairuiapi.com/hsstock/history/{symbol}/d/n/LICENCE-66D8-9F96-0C7F0FBCD073?st={startTime}&et={endTime}&lt=10"
response = requests.get(url)
data = response.json()
print(data)

