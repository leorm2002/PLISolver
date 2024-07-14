let linear = 'http://localhost:8080/solveLinearSimplex'
let linearI = 'http://localhost:8080/solveLinearInteger'
function calcolaL(){
    calcola(linear)
}

function calcolaLI(){
    calcola(linearI)

}
function toggleMathContainer() {
    const container = document.getElementById('math-container');
    container.style.display = container.style.display === 'block' ? 'none' : 'block';
}

function getVerso(verso) {
    if (verso == "<=") {
        return "LE"
    } else if (verso == ">=") {
        return "GE"
    } else {
        return "E"
    }
}

function getVincolo(vincoli, versi, i) {
    return {
        "vincolo": vincoli[i],
        "verso": getVerso(versi[i])
    }
}

function getVincoli(vincoli, versi, numConstraints) {
    out = []
    for (let i = 0; i < numConstraints; i++) {
        let vincolo = getVincolo(vincoli, versi, i)
        out.push(vincolo);
    }
    return out
}

function getFo(tipo, coeff) {
    return {
        "tipo": tipo,
        "c": coeff
    }
}

function calcola() {
    const numConstraints = document.getElementById('constraints').value;
    const numVariables = document.getElementById('variables').value;
    var vincoli = []
    var versi = []
    var coeff = []
    for (let i = 0; i < numConstraints; i++) {
        vincoli.push([])
    }
    for (let i = 0; i < numConstraints; i++) {
        for (let j = 0; j < numVariables; j++) {
            let id = "" + i + "_" + j;
            let value = document.getElementById(id).value;
            vincoli[i].push(value);
        }
        let id = "b" + i;
        let value = document.getElementById(id).value;
        vincoli[i].push(value)
        id = "v" + i;
        value = document.getElementById(id).value;
        versi.push(value)

    }
    for (let j = 0; j < numVariables; j++) {
        let id = "c" + (j + 1)
        let value = document.getElementById(id).value;
        coeff.push(value)
    }

    let tipo = document.getElementById("optimization-direction").value.toUpperCase()
    let vincoliRichiesta = getVincoli(vincoli, versi, numConstraints)
    let funzioneObb = getFo(tipo, coeff)
    let payload = {
        "funzioneObbiettivo": funzioneObb,
        "vincoli": vincoliRichiesta
    }

    inviaRichiesta(payload, link)
}

function populateTableau(data) {
    document.getElementById('titoloTab').innerText = 'Tableau ottimo'
    const tableau = document.getElementById('tableau');
    tableau.innerHTML = '';

    const numVariables = data[0].length - 1; // excluding the first column which is the value column
    const numRows = data.length;

    // Create header row
    const header = document.createElement('tr');
    header.appendChild(document.createElement('th')); // empty cell before -z
    ['-z', ...Array.from({ length: numVariables }, (_, i) => `x${i + 1}`)].forEach(headerText => {
        const th = document.createElement('th');
        th.textContent = headerText;
        header.appendChild(th);
    });
    tableau.appendChild(header);

    // Create data rows
    for (let i = 0; i < numRows; i++) {
        const row = document.createElement('tr');
        const rowHeader = document.createElement('td');
        rowHeader.textContent = `r${i}`;
        row.appendChild(rowHeader);

        data[i].forEach(cellText => {
            const td = document.createElement('td');
            td.textContent = cellText;
            row.appendChild(td);
        });
        tableau.appendChild(row);
    }
}

function testL() {
    let test = { "funzioneObbiettivo": { "tipo": "MIN", "c": ["1", "0", "1"] }, "vincoli": [{ "vincolo": ["1", "2", "0", "5"], "verso": "LE" }, { "vincolo": ["0", "1", "2", "6"], "verso": "E" }] }
    inviaRichiesta(test, linear)
}

function testLI() {
    let test = {"funzioneObbiettivo":{"tipo":"MIN","c":["0","-1"]},"vincoli":[{"vincolo":["3","2","6"],"verso":"LE"},{"vincolo":["-3","2","0"],"verso":"LE"}]}
    inviaRichiesta(test, linearI)
}
async function mostraTempo(time) {
    const tempo = document.getElementById('tempo');
    tempo.innerText = 'Tempo elaborazione: ' + time + "ms";
}

async function inviaRichiesta(payload, link) {
    const response = await fetch(link, {
        method: 'POST',
        headers: {
            'accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });
    const resp = await response.json();
    console.log(resp)
    // Load text into a vector of vector of strings
    populateTableau(resp.tableau)
    mostraTempo(resp.time)
    getSolution(resp.soluzione)
}

function updateTable() {
    const numConstraints = document.getElementById('constraints').value;
    const numVariables = document.getElementById('variables').value;

    if (numConstraints && numVariables) {
        // Show the table section
        document.getElementById('table-section').style.display = 'block';
        document.getElementById('objective-function').style.display = 'block';
        document.getElementById('parameters-section').style.display = 'block';

        // Generate table headers
        const tableHeader = document.getElementById('table-header');
        tableHeader.innerHTML = '';
        for (let i = 1; i <= numVariables; i++) {
            const th = document.createElement('th');
            th.textContent = `x${i}`;
            tableHeader.appendChild(th);
        }
        const thConstraint = document.createElement('th');
        thConstraint.textContent = 'Constraint';
        tableHeader.appendChild(thConstraint);
        const thValue = document.createElement('th');
        thValue.textContent = 'Value';
        tableHeader.appendChild(thValue);

        // Generate table body
        const tableBody = document.getElementById('table-body');
        tableBody.innerHTML = '';
        for (let i = 0; i < numConstraints; i++) {
            const tr = document.createElement('tr');
            for (let j = 0; j < numVariables; j++) {
                const td = document.createElement('td');
                const input = document.createElement('input');
                input.type = 'number';
                input.className = 'form-control';
                input.id = "" + i + "_" + j;
                td.appendChild(input);
                tr.appendChild(td);
            }
            // Add constraint dropdown
            const tdConstraint = document.createElement('td');
            const select = document.createElement('select');
            select.className = 'form-control';
            ['<=', '=', '>='].forEach(op => {
                const option = document.createElement('option');
                option.value = op;
                option.textContent = op;
                select.appendChild(option);
            });
            select.id = "v" + i;
            tdConstraint.appendChild(select);
            tr.appendChild(tdConstraint);

            // Add value input
            const tdValue = document.createElement('td');
            const inputValue = document.createElement('input');
            inputValue.type = 'number';
            inputValue.className = 'form-control';
            inputValue.id = "b" + i;
            tdValue.appendChild(inputValue);
            tr.appendChild(tdValue);

            tableBody.appendChild(tr);
        }

        // Generate objective function coefficients inputs
        const objectiveCoefficients = document.getElementById('objective-coefficients');
        objectiveCoefficients.innerHTML = '';
        for (let i = 1; i <= numVariables; i++) {
            const div = document.createElement('div');
            div.className = 'col-auto';
            const label = document.createElement('label');
            label.textContent = `x${i}`;
            const input = document.createElement('input');
            input.type = 'number';
            input.className = 'form-control';
            input.id = "c" + i
            div.appendChild(label);
            div.appendChild(input);
            objectiveCoefficients.appendChild(div);
        }
    } else {
        // Hide the table section if either dropdown is not selected
        document.getElementById('table-section').style.display = 'none';
        document.getElementById('objective-function').style.display = 'none';
        document.getElementById('parameters-section').style.display = 'none';
    }

}
function getSolution(sol){
    let out = []
    for (let i = 0; i < sol.length; i++) {
        
        out.push(`x${i + 1}: ${sol[i]}`)
    }
    console.log(out)
    let text = "Soluzione: " + out.join(", ")
    const solution = document.getElementById('solution');
    solution.innerText = text;
}
