#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>

//write grade and name to file
int write_grade(char*,char *, char*,int line);

//get name and grade form query
char * get_grade_name(char * query, char *name, char *);

//str spilt
char ** str_split(char *, const char, int *size);

//find line number with same name in file
int findLine(char * name);

//loads name list from file
char* loadList();
char *replaceWord(const char *s, const char *oldW, const char *newW);





int main(int argc, char *argv[])
{
    char fname[128], lname[126],*name_space;
    const char html[256]= "<!DOCTYPE html>\n\
<html>\n\
<head>\n\
<meta content=\"text/html;charset=utf-8\" http-equiv=\"Content-Type\">\n\
</head>\n\
<body>\n\
<p>grade %s, %s : %s submited</p>\n\
<h2>Grade List</h2>\
<p>%s</p>\n\
</body>\n\
</html>";
    char * grade =get_grade_name(argv[1], fname,lname);
    name_space = replaceWord(fname, "+", " ");
    int line=findLine(lname);
    write_grade(grade,name_space, lname,line);

    char * gradelist=loadList();
    printf(html, fname,lname, grade, gradelist);
}

int findLine(char * name)
{
    int line_no=0,s;
    FILE* file = fopen("src/root/grades.txt", "r"); /* should check the result */
    char line[256];

    while (fgets(line, sizeof(line), file)) {
        char  keyval[128];
        strcpy(keyval, line);
        char ** name_grade_map=str_split(keyval, ',', &s);
        if(strcmp(name_grade_map[0], name)==0)
        {
            free(name_grade_map);
            fclose(file);
            return line_no;
        }
        line_no++;
        free(name_grade_map);
    }

    fclose(file);
    return -1;
}

int write_grade(char * grade, char * fname, char* lname,int line)
{
    if (line < 0) {
        FILE *fPtr;
        fPtr = fopen("src/root/grades.txt", "a");
        fprintf(fPtr, "%s, %s:%s\n", lname, fname,grade);
        fclose(fPtr);
        return 1;
    }
    else {
        FILE *fptr1, *fptr2;
        int linectr = 0;
        char str[128];
        char  temp[] = "src/root/temp.txt";


        fptr1 = fopen("src/root/grades.txt", "r");
        if (!fptr1)
        {
            return 0;
        }
        fptr2 = fopen(temp, "w");
        if (!fptr2)
        {
            fclose(fptr1);
            return 0;
        }
        // copy all contents to the temporary file other except specific line
        while (!feof(fptr1))
        {
            strcpy(str, "\0");
            fgets(str,128, fptr1);
            if (!feof(fptr1))
            {

                if (linectr != line)
                {
                    fprintf(fptr2, "%s", str);
                }
                else
                {
                    fprintf(fptr2, "%s, %s:%s\n", lname,fname, grade);
                }
                linectr++;
            }
        }
        fclose(fptr1);
        fclose(fptr2);
        remove("src/root/grades.txt");
        rename(temp, "src/root/grades.txt");
        return 1;
    }
}

//this has memory leaks
char * get_grade_name(char * query, char * fname, char * lname)
{
    int a=0;
    char * ret_val=NULL;
    char ** key_val_list = str_split(query, '&', &a);

    if(key_val_list){
        int i=0;
        for(i=0; *(key_val_list+i);i++)
        {
            if(strstr(*(key_val_list+i), "grade") != NULL) {
                char buffer[256];
                strcpy(buffer, *(key_val_list+i));
                char ** pair = str_split(buffer, '=', &a);
                ret_val=malloc(strlen(pair[1])+1);
                strcpy(ret_val, pair[1]);
                free(pair);

            }
            if(strstr(*(key_val_list+i), "fname") != NULL) {
                char buffer[256];
                strcpy(buffer, *(key_val_list+i));
                char ** pair = str_split(buffer, '=', &a);
                strcpy(fname, pair[1]);
                free(pair);

            }
            if(strstr(*(key_val_list+i), "lname") != NULL) {
                char buffer[256];
                strcpy(buffer, *(key_val_list+i));
                char ** pair = str_split(buffer, '=', &a);
                strcpy(lname, pair[1]);
                free(pair);

            }

        }
        free(key_val_list);
    }

    return ret_val;

}

char** str_split(char* a_str, const char a_delim, int *size)
{
    char** result    = 0;
    size_t count     = 0;
    char* tmp        = a_str;
    char* last_comma = 0;
    char delim[2];
    delim[0] = a_delim;
    delim[1] = 0;

    /* Count how many elements will be extracted. */
    while (*tmp)
    {
        if (a_delim == *tmp)
        {
            count++;
            last_comma = tmp;
        }
        tmp++;
    }

    /* Add space for trailing token. */
    count += last_comma < (a_str + strlen(a_str) - 1);

    /* Add space for terminating null string so caller
       knows where the list of returned strings ends. */
    count++;

    result = malloc(sizeof(char*) * count);
    if (result)
    {
        size_t idx  = 0;
        char* token = strtok(a_str, delim);

        while (token) {
          assert(idx < count);
          *(result + idx++) = strdup(token);
          token = strtok(0, delim);
        }
        assert(idx == count - 1);
        *(result + idx) = 0;
    }
    return result;

}

char* loadList()
{
    char * buffer = 0;
    long length;
    FILE * f = fopen ("src/root/grades.txt", "rb");

    if (f)
    {
        fseek (f, 0, SEEK_END);
        length = ftell (f);
        fseek (f, 0, SEEK_SET);
        buffer = malloc (length);
        if (buffer)
        {
            fread (buffer, 1, length, f);
        }
        fclose (f);
    }
   char* file_html= replaceWord(buffer, "\n", "<br/>");
    return file_html;
}

char *replaceWord(const char *s, const char *oldW,
                                 const char *newW)
{
    char *result;
    int i, cnt = 0;
    int newWlen = strlen(newW);
    int oldWlen = strlen(oldW);

    // Counting the number of times old word
    // occur in the string
    for (i = 0; s[i] != '\0'; i++)
    {
        if (strstr(&s[i], oldW) == &s[i])
        {
            cnt++;

            // Jumping to index after the old word.
            i += oldWlen - 1;
        }
    }

    // Making new string of enough length
    result = (char *)malloc(i + cnt * (newWlen - oldWlen) + 1);

    i = 0;
    while (*s)
    {
        // compare the substring with the result
        if (strstr(s, oldW) == s)
        {
            strcpy(&result[i], newW);
            i += newWlen;
            s += oldWlen;
        }
        else
            result[i++] = *s++;
    }

    result[i] = '\0';
    return result;
}
