GW.sidenav = {

    sidenavContext: {
        // init
        code: "",
        confidentialField: true,
        category: "",
        codeType: "",
        processId: "",
        processName: "",
        history: [],
        hist_ids: []
    },

    getData: (id) => {
        const TYPE = "process";
        let nodeIdentifier = id.split("-")[0];
        let code, owner, confidentialField, category, codeType, processId, processName;

        $.ajax({
            url: "detail",
            method: "POST",
            data: "type=" + TYPE + "&id=" + nodeIdentifier
        }).done((response) => {
            let parsedMessage = GW.general.parseResponse(response);
            codeType = parsedMessage.lang === null ? parsedMessage.description : parsedMessage.lang
            code = parsedMessage.code;
            if (code && code.includes("\\\"")) {
                code = GW.process.unescape(code);
            }
            processId = parsedMessage.id;
            processName = parsedMessage.name;
            owner = parsedMessage.owner;
            confidentialField = parsedMessage.confidential !== "FALSE";

            GW.sidenav.sidenavContext['code'] = code;
            GW.sidenav.sidenavContext['confidentialField'] = confidentialField;
            GW.sidenav.sidenavContext['category'] = category;
            GW.sidenav.sidenavContext['codeType'] = codeType;
            GW.sidenav.sidenavContext['processId'] = processId;
            GW.sidenav.sidenavContext['processName'] = processName;

            console.log('Sidenav context updated.')
        })
    },

    getHistory: (historyId) => {
        $.ajax({
            url: "log",
            method: "POST",
            data: "type=host&id=" + historyId
        }).done((response) => {
            if (response === "") {
                alert("Cannot find history for this code block.");
                return;
            }
            let parsedResponse = $.parseJSON(response);
            let code = parsedResponse.output;
            if (code && typeof code !== undefined) {
                if (typeof code != "object") {
                    code = $.parseJSON(code);
                }
                GW.sidenav.history.push(code);
                GW.sidenav.hist_ids.push(historyId);
                if (GW.sidenav.history.length == 2) {

                }

            }
        })
    }
}