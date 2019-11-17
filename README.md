# WorkflowManager

/* Requirements */

Un sistem care simuleaza executia unor secvente de activitati dintr-o
organizatie. Secventa de activitati este descrisa ca o succesiune de
stari legate prin tranzitii (masina de stari). Fiecare stare reprezinta o
activitate, iar activitatile se pot desfasura in paralel. O activitate
consta in lansarea in executie a unei metode specifice dintr-o
clasa Java incarcata de pe disc. 

In tot sistemul este vizibila o resursa
care memoreaza un set de variabile de mediu globale. Trecerea dintr-o
stare in alta se poate face conditionat de valorile variabilelor de
mediu. De asemenea, activitatea dintr-o stare poate utiliza
informatile din variabilele de mediu (setate de alte stari precedente)
pentru a-si indeplini functia.

Dintr-o stare se poate trece in mai multe stari care vor
fi atinse in paralel.

Masina de stari precum si numele claselor care trebuie
incarcate pentru fiecare activitate sunt specificate prin elemente de 
configurare.

Exemplu: Intr-o firma de software documentatia unei aplicatii trebuie sa
treaca prin anumite faze pentru a fi aprobata. Starea initiala A este
scrierea documentatiei. Starea B este discutarea primei versiuni de
catre echipa care a implementat aplicatia. Daca echipa isi da acordul,
se trece in starea C, trimiterea spre publicare. Daca nu, se revine in
starea A. Din starea C se trece in paralel in starile D si E:
D=publicare pe internet, C=tiparire si trimitere spre client.

Tranzitiile sunt:
A->B (neconditionat)
B->A (daca echipa nu e de acord)
B->C (daca echipa e de acord)
C->D
C->E

In starea A scrierea documentatiei poate fi simulata apeland metoda
run() a unei clase ScrieDocumentatie. In starea C se poate apela
metoda run() a unei clase EchipaDiscuta care va pune intr-o variabila
de mediu decizia echipei. Sistemul citeste variabila de mediul pentru
a afla in care din starile C sau A se trece. Mediul poate fi simulat
ca un obiect accesibil tuturor starilor, ca un fisier pe disc etc.
