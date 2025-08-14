       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGV0001.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT INPUT-FILE ASSIGN TO 'STUDENTSEQ.DAT'
               ORGANIZATION IS LINE SEQUENTIAL.
           SELECT VSAM-FILE ASSIGN TO 'STUDENT.VSAM'
               ORGANIZATION IS INDEXED
               ACCESS MODE IS DYNAMIC
               RECORD KEY IS STUDENT-ID.

       DATA DIVISION.
       FILE SECTION.
       FD INPUT-FILE.
       01 INPUT-RECORD        PIC X(80).

       FD VSAM-FILE.
       01 STUDENT-REC.
          05 STUDENT-ID          PIC X(10).
          05 STUDENT-NAME        PIC X(30).
          05 STUDENT-BIRTHDAY    PIC X(10).
          05 STUDENT-COURSE      PIC X(10).
          05 STUDENT-INS-DATE    PIC 9(8).
          05 STUDENT-UPD-DATE    PIC 9(8).

       WORKING-STORAGE SECTION.
       01 EOF-FLAG              PIC X VALUE 'N'.
       01 WS-ID                 PIC X(10).
       01 WS-NAME               PIC X(30).
       01 WS-BIRTHDAY           PIC X(10).
       01 WS-COURSE             PIC X(10).
       01 ZEROES-STR            PIC X(8) VALUE '00000000'.
       01 FIELDS-DATE           PIC 9(8) VALUE 20250801.

       PROCEDURE DIVISION.
       BEGIN.
           OPEN INPUT INPUT-FILE.
           OPEN OUTPUT VSAM-FILE.

           PERFORM UNTIL EOF-FLAG = 'Y'
               READ INPUT-FILE
                   AT END MOVE 'Y' TO EOF-FLAG
                   NOT AT END
                       UNSTRING INPUT-RECORD DELIMITED BY ","
                           INTO WS-ID, WS-NAME, WS-BIRTHDAY, WS-COURSE
                       MOVE WS-ID         TO STUDENT-ID
                       MOVE WS-NAME       TO STUDENT-NAME
                       MOVE WS-BIRTHDAY   TO STUDENT-BIRTHDAY
                       MOVE WS-COURSE     TO STUDENT-COURSE
                       MOVE FIELDS-DATE   TO STUDENT-INS-DATE
                       MOVE ZEROES        TO STUDENT-UPD-DATE
                       WRITE STUDENT-REC
               END-READ
           END-PERFORM.

           CLOSE INPUT-FILE.
           CLOSE VSAM-FILE.

           DISPLAY "VSAM CONVERSION COMPLETE.".

           GOBACK.
       END PROGRAM PRGV0001.
