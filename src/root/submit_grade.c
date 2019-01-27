#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>
int write_grade(char*);
//char ** parse_query(char *);
char * get_grade(char * query);
char ** str_split(char *, const char, int *size);
void substr(char* str, char* sub , int start, int len){
    memcpy(sub, &str[start], len);
    sub[len] = '\0';
}

int main(int argc, char *argv[])
{
    const char html[120]= "<!DOCTYPE html><html><body><p>grade %s submited</p></body></html>";
    char * grade =get_grade(argv[1]);
    write_grade(grade);
    printf(html, grade);
}

int write_grade(char * grade)
{
    FILE *fPtr;
    fPtr = fopen("grades.txt", "a");
    fprintf(fPtr,"%s\n", grade);
    fclose(fPtr);
    return 1;
}

//this has memory leaks
char * get_grade(char * query)
{
    int a=0;
    char * ret_val=NULL;
    char ** key_val_list = str_split(query, '&', &a);
    if(key_val_list){
        int i=0;
        for(i=0; *(key_val_list+i);i++)
        {
            if(strstr(*(key_val_list+i), "grade") != NULL) {
                char ** pair = str_split(*(key_val_list+i), '=', &a);
                ret_val=malloc(strlen(pair[1])+1);
                strcpy(ret_val, pair[1]);
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
