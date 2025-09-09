       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGQ0005.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT VSAM-FILE ASSIGN TO 'STUDENT.VSAM'
               ORGANIZATION IS INDEXED
               ACCESS MODE IS SEQUENTIAL
               RECORD KEY IS STUDENT-ID.

       DATA DIVISION.
       FILE SECTION.
       FD VSAM-FILE.
       01 STUDENT-REC.
           05 STUDENT-ID          PIC X(4).
           05 STUDENT-NAME        PIC X(20).
           05 STUDENT-BIRTHDAY    PIC X(8).
           05 STUDENT-COURSE      PIC X(15).

       WORKING-STORAGE SECTION.
       01 EOF-FLAG            PIC X VALUE 'N'.
       01 WS-COUNT            PIC 9(4) VALUE ZERO.
       01 WS-DISPLAY-LINE     PIC X(100).

       PROCEDURE DIVISION.
       MAIN-LOGIC.
           OPEN INPUT VSAM-FILE
           MOVE 'N' TO EOF-FLAG
           MOVE 0 TO WS-COUNT

           DISPLAY "ID | NAME | BIRTHDAY | COURSE"


           PERFORM UNTIL EOF-FLAG = 'Y'
               READ VSAM-FILE NEXT
                   AT END
                       MOVE 'Y' TO EOF-FLAG
                   NOT AT END
                       STRING
                           STUDENT-ID DELIMITED BY SPACE
                           " | "
                           STUDENT-NAME DELIMITED BY SPACE
                           " | "
                           STUDENT-BIRTHDAY DELIMITED BY SPACE
                           " | "
                           STUDENT-COURSE DELIMITED BY SPACE
                           INTO WS-DISPLAY-LINE

                       END-STRING
                       DISPLAY "----------------------------"
                       DISPLAY WS-DISPLAY-LINE
                       ADD 1 TO WS-COUNT
               END-READ
           END-PERFORM

           DISPLAY "TOTAL STUDENTS: "
           DISPLAY WS-COUNT

           CLOSE VSAM-FILE
           GOBACK.
