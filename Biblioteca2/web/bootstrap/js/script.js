function verf(a) {
    switch (a) {
        case 1:
            document.getElementById("add").style.display = 'block';
            document.getElementById("elim").style.display = 'none';
            break;
        case 2:
            document.getElementById("add").style.display = 'none';
            document.getElementById("elim").style.display = 'block';
            break;
    }

}