// Ali 
// LZ 77

#include <bits/stdc++.h>
#include<string>
#include<iostream>
#include<vector>
#define ll long long
#define f(i,n) for(int i=0 ;i<n ;i++)
#define f1(i,n) for(int i=1 ;i<=n ;i++)
#define endl "\n"
#define no "NO\n"
#define yes "YES\n"

using namespace std;
class tag{
    public:
        int position ;
        int length ;
        char next_symbol ;

};

int window ;
string stream ;
bool check(vector<char> &v , int j ){
    bool tt = 0 ;
    bool c_inside = 1 ;
    for (int k=0; k < v.size() ; k++){
        if(stream[k+j]!=v[k])c_inside = false ;
    }
    if(c_inside==1)return true ;
    return false ;
}
// stream
// ABAABABAABBBBBBBBBBBBA  90
// CABRACADABRARRARRAD    112
int Max_Search (int position){
    if(position-window < 0 )return position; // not all search window is valid
    else return window;   // all window valid
}
int Max_Look_Ahead(int position){
    if(position+window > stream.size())return stream.size()-position ; // size-position // maximum number is the difference not the all window
    else return window ; // alll window is valid
}
int main(){
                    /**     compression    **/ 

        cout << "enter string stream\n";
        cin >> stream;
        cout << " enter window size\n";
        //cin >> window;  // if you want to take window as input bu in our case we use whole window  w = 22
        window = 22;
        //window /=2 ;
        //int S_window = window/2;
        //int LA_window= window/2;

        vector<tag> Tags;
        for(int current_item=0; current_item<stream.size(); current_item++){
            int max_look_ahead = Max_Look_Ahead(current_item);
            int max_search = Max_Search(current_item);

            tag current_tag;
            current_tag.length = 0 ;
            current_tag.position = 0;
            current_tag.next_symbol = stream[current_item];


            for(int position = 1 ; position <= max_search; position++ ){
                int length = 0 ;
                while(stream[current_item + length] == stream[current_item - position + length ] && length < max_look_ahead ){
                    length++;
                }
                if(length > current_tag.length){
                    current_tag.length = length;
                    current_tag.position = position;
                    current_tag.next_symbol = stream[current_item + length ];
                }
            }
            current_item = current_item + current_tag.length ;
            Tags.push_back(current_tag);

        }
        int l = 0, p = 0;
        f(i,Tags.size()){
            cout << Tags[i].position << "  " << Tags[i].length << "  " << Tags[i].next_symbol << endl;
            l = max(l,Tags[i].length);
            p = max(p, Tags[i].position);
        }
        //cout << sizeof(7);
        int length_bits = 0;
        for(int i=0 ; i<10 ; i++){
            if(l>>i&1){
                length_bits= max(length_bits,i+1);
            }
        }
        int position_bits = 0;
        for(int i=0 ; i<10 ; i++){
            if(p>>i&1){
                position_bits= max(position_bits,i+1);
            }
        }
        cout << "\n total size = position bits+ length bits + sybmol bits: ("<<position_bits<<" + "<<length_bits << " + "<<" 8  ) * tag number \n";
        cout << " \t   =   \t " << (position_bits+length_bits+8) * Tags.size() << endl ;


        //cout << "\n enter number of tags \n"  ;
        int size_  = 8;
        //cin >> size_ ;
        cout << " enter tags \n";
        vector<tag>c_tags(size_) ;
        char dummy ;
        string dummy_string ;
        for(int i =0 ; i<size_; i++){
            cin >> dummy_string ;
            // <p,l,"c">
            // 012345678
            c_tags[i].position = (dummy_string[1]-'0');
            c_tags[i].length   = dummy_string[3]-'0';
            c_tags[i].next_symbol = dummy_string[6];

        }
        string s ="";

        for(int i = 0 ; i<size_; i++){   // ABAABAB
            int counter =0;
            for(int j=s.size()-c_tags[i].position ; counter<c_tags[i].length ; j++, counter++){
                s+=s[j] ;
            }
            s+=c_tags[i].next_symbol;
        }
        cout << s << endl ;

//<0,0,�A�> <0,0,�B�> <2,1,�A�> <3,2,�B�> <5,3,�B�> <2,2,�B�> <5,5,�B�> <1,1,�A�>     //ABAABABAABBBBBBBBBBBBA
//<0,0,�C�> <0,0,�A�> <0,0,�B�> <0,0,�R�> <3,1,�C�> <2,1,�D�> <7,4,�R�> <3,5,�D�>     //CABRACADABRARRARRAD



/*
            cin>>dummy ; // <
            cin >> c_tags[i].position;
            cin>>dummy ; // ,
            cin >> c_tags[i].length;
            //cin>>dummy ; //
            cin >> dummy_string ; // ,"c">
            c_tags[i].next_symbol = dummy_string[2] ;// second item is our symbol
            */


/*        for(int i=0; i<stream.size(); i++){
            int temp_i = i ;
            int temp_pos = 0 ;
            vector<char> key;
            key.push_back(stream[i]);
            //int counter = window/2 ; // for look ahead
            for(int j= i-key.size() ; j>=0 && j>=i-window/2 ; j--){
                if(check(key,j)&& temp_i<stream.size() && temp_i <= i+window/2){
                    //while(check(key,j)&&temp_i<= i+window/2 && i<stream.size()){
                        key.push_back(stream[temp_i+1]);
                        temp_i++;
                        temp_pos = i-j ;
                        j = i-key.size();                          // A B A A B A B A A B B B B B B B B B B B B A
                    //}
                }
                i = temp_i;
            }
            tag new_tag ;
            new_tag.length = key.size()-1;
            new_tag.position = temp_pos;
            new_tag.next_symbol = key[key.size()-1] ;//stream[i] ;
            Tags.push_back(new_tag) ;
        }

        for(int i=0 ; i<Tags.size(); i++){
            cout <<   Tags[i].position  << "  " <<Tags[i].length << "  " << Tags[i].next_symbol << endl ;
        }


*/


 /*



        int sz = stream.size();
        f(i,sz){
            int max_left = max(0, i-S_window);
            int max_right = min(sz-1, i+LA_window);
            int j = i;
            tag wewe;
            wewe.length = 0;
            wewe.position = 0;
            wewe.next_symbol = stream[i];
            int tmp_size=0 , count =0 ;
            for(; tmp_size<= max_right ; tmp_size++){ // number of matching elements

                                                                        //for(int matching = j ; matching<=j+tmp_size ; matching++){ // matching in LookAhead Window
                    for(int x = j-tmp_size-1 ; x >= 0 && x >=max_left ; x--){ // x should incremented by tmp_size
                        int l = 0, p=0 ;
                        char n = '0';
                        int first = x , second = j ;
                        while(stream[first++]==stream[second++] && first <= j){
                            l++;
                        }
                        if(l>=wewe.length){
                             wewe.length = l;
                             wewe.position = i-l;
                             wewe.next_symbol = stream[j+l];
                             count = tmp_size;
                             break;
                        }
                    }
            }
            i+=count;
            Tags.push_back(wewe);
    }
    f(i,Tags.size())
        cout << Tags[i].position << "  "<< Tags[i].length  << "  " << Tags[i].next_symbol << "  \n" ;
    cout << endl;

*/
    return 0;
}
