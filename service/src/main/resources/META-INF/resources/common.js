let linear = 'http://localhost:8080/solveLinearSimplex'
let linearI = 'http://localhost:8080/solveLinearInteger'
function calcolaL() {
    calcola(linear)
}

function calcolaLI() {
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

function calcola(link) {
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
        "vincoli": vincoliRichiesta,
        "parameters": getParameters()
    }


    inviaRichiesta(payload, link)
}

function populateTableau(data, tableauId) {
    if (data == null) {
        return
    }
    document.getElementById('titoloTab').innerText = 'Tableau ottimo'
    const tableau = document.getElementById(tableauId);
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
        let j = 0;
        data[i].forEach(cellText => {
            const td = document.createElement('td');
            td.textContent = cellText;
            if (j != 0 && i != 0) {
                // Imposto più scuro
                td.style = "background-color: var(--primD)!important;"
            }
            row.appendChild(td);
            j++;
        });
        tableau.appendChild(row);
    }
}

function testL() {
    console.log(getParameters())
    let test =
    {
        "funzioneObbiettivo": {
            "tipo": "MIN",
            "c": [
                "1",
                "0",
                "1"
            ]
        },
        "vincoli": [
            {
                "vincolo": [
                    "1",
                    "2",
                    "0",
                    "5"
                ],
                "verso": "LE"
            },
            {
                "vincolo": [
                    "0",
                    "1",
                    "2",
                    "6"
                ],
                "verso": "E"
            }
        ],
        "parameters": getParameters()
    }

    inviaRichiesta(test, linear)
}

function testLI() {
    let test = { "funzioneObbiettivo": { "tipo": "MIN", "c": ["0", "-1"] }, "vincoli": [{ "vincolo": ["3", "2", "6"], "verso": "LE" }, { "vincolo": ["-3", "2", "0"], "verso": "LE" }] }
    test["parameters"] = getParameters()
    inviaRichiesta(test, linearI)
}
async function mostraTempo(time) {
    const tempo = document.getElementById('tempo');
    tempo.innerText = 'Tempo elaborazione: ' + time + "ms";
}

async function inviaRichiesta(payload, link) {
    clean('output')
    clean('tableau')
    clean('solution')
    clean('tempo')
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
    if (resp.error != null) {
        alert(resp.error)
    } else {
        populateTableau(resp.tableau, 'tableau')
        mostraTempo(resp.time)
        getSolution(resp.soluzione)

    }
    // Esempio di utilizzo
    displayFormattedStrings(resp.passiRisoluzione);
    responseData = resp.passiRisoluzione;
    document.getElementById('downloadButton').style.display = 'block';
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
function getSolution(sol) {
    let out = []
    for (let i = 0; i < sol.length; i++) {

        out.push(`x${i + 1}: ${sol[i]}`)
    }
    console.log(out)
    let text = "Soluzione: " + out.join(", ")
    const solution = document.getElementById('solution');
    solution.innerText = text;
}

function formatString(str) {
    return str
        .replace(/\n/g, '<br>')
        .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;');
}
function clean(containerId = 'output') {
    const container = document.getElementById(containerId);
    container.innerHTML = '';

}
function displayFormattedStrings(strings, containerId = 'output') {
    const container = document.getElementById(containerId);
    let i = 0;
    strings.forEach(str => {
        let msg = str.message
        if (msg != null) {
            const preElement = document.createElement('pre');
            preElement.innerHTML = msg;
            container.appendChild(preElement);
        }
        let tableau = str.tableau
        if (tableau != null) {
            console.log(tableau)
            const preElement = document.createElement('table');
            preElement.className = 'tableau'
            preElement.id = 'tableau' + i + containerId
            i += 1;
            container.appendChild(preElement);
            populateTableau(tableau, preElement.id)
        }
        let sottoProblema = str.passaggiIntermedi
        if (sottoProblema != null) {
            let sectionId = "passaggio" + i
            let section = getCollapsableSection(sectionId);

            container.appendChild(section);
            displayFormattedStrings(sottoProblema, sectionId)
            document.getElementById(sectionId).appendChild(document.createElement('hr'));


        }

        container.appendChild(document.createElement('br'));
    });
}
function getCollapsableSection(idSezione) {
    let container = document.createElement('div');
    container.className = 'collapsableSection'
    let button = document.createElement('button');
    button.className = 'btn btn-primary'
    button.innerText = 'Mostra passaggi intermedi'
    let div = document.createElement('div');

    button.onclick = function () {
        let content = document.getElementById(idSezione);
        if (content.style.display === "block") {
            content.style.display = "none";
            this.innerText = 'Mostra passaggi intermedi'
        } else {
            content.style.display = "block";
            this.innerText = 'Nascondi passaggi intermedi'
        }
    }
    container.appendChild(button)
    container.appendChild(document.createElement('br'));
    container.appendChild(document.createElement('br'));

    div.style.display = "none";
    div.className = 'collapsableContent'
    div.id = idSezione

    container.appendChild(div)
    return container

}

function downloadJSON() {
    // Converti l'oggetto in una stringa JSON
    const jsonString = JSON.stringify(responseData, null, 2);

    // Crea un Blob con il contenuto JSON
    const blob = new Blob([jsonString], { type: 'application/json' });

    // Crea un URL oggetto per il Blob
    const url = URL.createObjectURL(blob);

    // Crea un elemento <a> nascosto
    const link = document.createElement('a');
    link.href = url;
    link.download = 'response.json'; // Nome del file da scaricare

    // Aggiungi il link al documento, clicca programmaticamente e rimuovilo
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    // Rilascia l'URL oggetto
    URL.revokeObjectURL(url);
}

// Aggiungi un event listener al bottone
document.getElementById('downloadButton').addEventListener('click', downloadJSON);

function getParameters() {
    console.log("Ionon");
    let numeroMassimo = 1000;
    let fp = false;
    let int = true;
    // if document.getElementById('max-iterations') is numeric
    if (document.getElementById('max-iterations') != null) {
        console.log("Io");
        if (document.getElementById('max-iterations').value !== "") {
            numeroMassimo = document.getElementById('max-iterations').value;
        }
    }
    if (document.getElementById('fp') != null) {
        fp = document.getElementById('fp').checked;
        console.log(fp)
    }

    if (document.getElementById('intermedi') != null) {
        int = document.getElementById('intermedi').checked;
    }


    return {
        'maxIterazioni': numeroMassimo,
        'passaggiIntermedi': int,
        'floatingPoint': fp
    }
}