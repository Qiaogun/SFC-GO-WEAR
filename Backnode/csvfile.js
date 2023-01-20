const fs = require('fs');

let currentCsvFileName = '';

function createNewCsv(fileName) {
  currentCsvFileName = fileName;
  fs.writeFile(fileName, '', (err) => {
    if (err) throw err;
    console.log(`${fileName} has been created`);
  });
}

function handleCreateCsv(req, res) {
  const fileName = 'newfile.csv'
  createNewCsv(fileName);
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end(`New CSV file ${fileName} created and saved in variable currentCsvFileName`);
}
