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
        hist_ids: [],
        comparison: null
    },

    sideNavCodePreview: () => {
        s
    },

    close: () => {
        let sidenav = document.getElementById('sidenav-editor');
        sidenav.style.display = 'none';
    },

    show: () => {
        let sidenav = document.getElementById('sidenav-editor');
        sidenav.style.display = 'block';
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

            console.log(`Sidenav context updated for node ${id}`)

            document.getElementById('sidenav-code-type').innerText = codeType;

            let processIdElement = document.getElementById('sidenav-process-id');
            processIdElement.value = processId;
            processIdElement.style.backgroundColor = 'white';
            processIdElement.style.margin = '0';
            processIdElement.style.width = '8rem';
            processIdElement.style.height = '28px';
            processIdElement.style.paddingLeft = '10px';
            processIdElement.style.borderRadius = '2px';
            processIdElement.style.fontWeight = '600';
            processIdElement.style.border = '0px';


            document.getElementById('sidenav-process-name').innerText = processName;
            document.getElementById('sidenav-confidential-field').innerText = confidentialField
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
                if (GW.sidenav.history.length === 2) {
                    const notebook1 = nb.parse(GW.sidenav.hist_ids[0].content);

                    const notebook2 = nb.parse(GW.sidenav.hist_ids[1].content);

                    const rendered1 = notebook1.render();

                    const rendered2 = notebook2.render();

                    let content = '<div class="modal-body">'+

                        '	<div class="row">'+

                        '		<div class="col-md-6">'+

                        $(rendered1).html()+

                        '		</div>'+

                        '		<div class="col-md-6">'+

                        $(rendered2).html()+

                        '		</div>'+

                        '	</div>'+

                        '</div>';

                    content += '<div class="modal-footer">' +
                        "	<button type=\"button\" id=\"sidenav-compare-download-btn\" class=\"btn btn-outline-primary\">Download</button> "+
                        "	<button type=\"button\" id=\"sidenav-compare-cancel-btn\" class=\"btn btn-outline-secondary\">Cancel</button>"+
                        '</div>';


                    GW.sidenav.comparison = GW.process.createJSFrameDialog(800, 600, content, "History Comparison " + GW.sidenav.hist_ids[0] +
                        " vs " + GW.sidenav.hist_ids[1]);

                    $("#sidenav-compare-download-btn").click(function(){

                        GW.host.downloadJupyter(GW.sidenav.hist_ids[0]);
                        GW.host.downloadJupyter(GW.sidenav.hist_ids[1]);

                    })

                    $("#sidenav-compare-cancel-btn").click(function(){
                        if (GW.sidenav.comparison) {
                            GW.sidenav.comparison.closeFrame();
                        }
                    });
                }

            }
        })
    },

    parseSidenavCodeBlock: (processId, processName, codeType, code) => {

        $("#sidenav-code-embed").html("");
        $("#sidenav-code-embed").css({'overflow-y': 'scroll'});
        $('#sidenav-process_code_window').css("background-color", "white");
        if (codeType === "jupyter") {
            $("#sidenav-code-embed").append(
                `<p style="margin:5px;" class="pull-right">
                    <span class="badge badge-secondary">double click</span> 
                    to edit
                     <span class="badge badge-secondary">Ctrl+Enter</span> to save
                     <i class="fa fa-upload subalignicon"   data-toggle="tooltip" title="upload a new notebook to replace the current one" onclick="GW.process.uploadAndReplaceJupyterCode();"></i></p><br/>`
            )

            if(code != null && code !== "null") {

                code = GW.general.parseResponse(code);

                GW.process.jupytercode = code;

                var notebook = nb.parse(code);

                var rendered = notebook.render();

                $("#sidenav-code-embed").append(rendered);

                nb.postlisten();

                var newjupyter = nb.getjupyterjson();

            } else if (codeType === "builtin"){
                code = code.replace(/\\/g, '\\\\');
                code = GW.general.parseResponse(code);
                let cont = `
                    <label for="builtinprocess" class="col-sm-4 col-form-label control-label" style="font-size:12px;" >
                        Select a process: 
                    </label>
                    <div class="col-sm-8"> <select class="form-control builtin-process" id="builtin_processes">`;

                for(var i=0;i<GW.process.builtin_processes.length;i++){

                    var se = "";

                    if(GW.process.builtin_processes[i].operation == code.operation){

                        se = " selected=\"selected\" ";

                    }

                    cont += '    		<option value="'+
                        GW.process.builtin_processes[i].operation +
                        '"  ' + se + ' >'+
                        GW.process.builtin_processes[i].operation +
                        '</option>';

                }
            }
        }


    }

}