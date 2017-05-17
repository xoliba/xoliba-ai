

const rows = 7;

function create2DArray() {
    var arr = new Array(rows);

    for (var i=0;i<rows;i++) {
        arr[i] = new Array(rows);
        for (var j=0;j<rows;j++) {
            arr[i][j]=0;
        }
    }

    return arr;
}