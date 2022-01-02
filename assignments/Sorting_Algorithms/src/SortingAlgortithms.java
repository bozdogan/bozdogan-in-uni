/*
    BIM304 Computer Algorithm Design
    2020 - Assignment 3

    Bora Özdoğan 2020-05-29
*/

public class SortingAlgortithms {

/*  implement sorting algorithms below
•	Bubble Sort
•	Selection Sort
•	Insertion Sort
•	Merge Sort
•	Quick Sort
•	Heap Sort
•	Counting Sort
•	Radix Sort

   Fill in the method.
   Implement sorting algorithms.
   You can use extra method.

*/
    
    // NOTE(bora): I need a global variable to store timing info for the summary.
    private int[] chrono = new int[8];

    public void BubbleSort(int A[]) {
//WRITE YOUR BUBBLE SORT CODE HERE

        long timer = System.currentTimeMillis();
        for(int i = 0; i < A.length-1; i++)
        for(int j = 0; j < (A.length-i) - 1; j++)
            if(A[j] > A[j+1])
            {
                int tmp = A[j];
                A[j] = A[j+1];
                A[j+1] = tmp;
            }
        chrono[0] = (int)(System.currentTimeMillis() - timer);
    }

    public void SelectionSort(int A[]) {
//WRITE YOUR SELECTION SORT CODE HERE

        long timer = System.currentTimeMillis();
        int min;
        for(int i = 0; i < A.length-1; i++)
        {
            min = i;
            for(int j = i+1; j < A.length; j++)
                if(A[j] < A[min])
                    min = j;
            
            if(min != i)
            {
                int tmp = A[i];
                A[i] = A[min];
                A[min] = A[i];
            }
        }
        chrono[1] = (int)(System.currentTimeMillis() - timer);
    }

    public void InsertionSort(int A[]) {
//WRITE YOUR INSERTION SORT CODE HERE

        long timer = System.currentTimeMillis();
        for(int i = 1; i < A.length; i++)
        {
            int key = A[i];
            
            int j;
            for(j = i-1; j >= 0 && A[j] > key; j--)
            {
                A[j+1] = A[j];
            }
            A[j+1] = key;
        }
        chrono[2] = (int)(System.currentTimeMillis() - timer);
    }

    private void _MergeSort_Merge(
            int[] arr, 
            int left, int mid, int right)
    {
        int[] L = new int[mid-left + 1];
        int[] R = new int[right-mid];

        for(int i = 0; i < L.length; i++)
            L[i] = arr[left+i];

        for(int i = 0; i < R.length; i++)
            R[i] = arr[mid+1 + i];
        

        int i, j, k;
        for(i = 0, j = 0, k = left;  
            i < L.length && j < R.length;
            k++)
        {
            if(L[i] <= R[j])
            {
                arr[k] = L[i];
                i++;
            }
            else
            {
                arr[k] = R[j];
                j++;
            }
        }


        for(; i < L.length; i++, k++)
            arr[k] = L[i];

        for(; j < R.length; j++, k++)
            arr[k] = R[j];
    }

    private void _MergeSort_Sort(int[] arr, int left, int right)
    {
        if(left < right)
        {
            int mid = (left+right) / 2;
            _MergeSort_Sort(arr, left, mid);
            _MergeSort_Sort(arr, mid+1, right);
            _MergeSort_Merge(arr, left, mid, right);
        }
    }
    
    public void MergeSort(int A[]) {
//WRITE YOUR MERGE SORT CODE HERE

        long timer = System.currentTimeMillis();
        _MergeSort_Sort(A, 0, A.length-1);
        chrono[3] = (int)(System.currentTimeMillis() - timer);
    }

    private int _QuickSort_Partition(int[] arr, int left, int right)
    {
        int pivot = arr[right];

        int i, j;
        for(i = left-1, j = left; j <= right-1; j++)
            if(arr[j] < pivot)
            {
                int tmp = arr[++i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        
        int tmp = arr[i+1];
        arr[i+1] = arr[j];
        arr[j] = tmp;

        return i+1;
    }

    private void _QuickSort_Sort(int[] arr, int left, int right)
    {
        if(left < right)
        {
            int sep = _QuickSort_Partition(arr, left, right);

            _QuickSort_Sort(arr, left, sep-1);
            _QuickSort_Sort(arr, sep+1, right);
        }
    }

    public void QuickSort(int A[]) {
//WRITE YOUR QUICK SORT CODE HERE

        long timer = System.currentTimeMillis();
        _QuickSort_Sort(A, 0, A.length-1);
        chrono[4] = (int)(System.currentTimeMillis() - timer);
    }

    private void _HeapSort_Heapify(int[] arr, int n, int root)
    {
        int max = root;
        int left  = 2*root + 1;
        int right = 2*root + 2;

        if(left < n && arr[left] > arr[max])
            max = left;
        
        if(right < n && arr[right] > arr[max])
            max = right;
        
        if(max != root)
        {
            int tmp =arr[root];
            arr[root] = arr[max];
            arr[max] = tmp;
            _HeapSort_Heapify(arr, n, max);
        }
    }

    public void HeapSort(int A[]){
//WRITE YOUR HEAP SORT CODE HERE

        long timer = System.currentTimeMillis();
        for(int i = A.length/2; i >= 0; i--)
            _HeapSort_Heapify(A, A.length, i);
        
        for(int i = A.length-1; i > 0; i--)
        {
            int tmp = A[0];
            A[0] = A[i];
            A[i] = tmp;
            _HeapSort_Heapify(A, i , 0);
        }
        chrono[5] = (int)(System.currentTimeMillis() - timer);
    }

    public void CountingSort( int A[]){
        //WRITE YOUR COUNTING SORT CODE HERE

        long timer = System.currentTimeMillis();
        int maxvalue = A[0];
        for(int i = 0; i < A.length; i++)
        {  
            if(A[i] > maxvalue)
                maxvalue = A[i]; 
        }
        int[] scoretable = new int[maxvalue+1];
        int[] sorted = new int[A.length];

        for(int i = 0; i < A.length; i++)
            scoretable[A[i]]++;
        
        for(int i = 1; i <= maxvalue; i++)
            scoretable[i] += scoretable[i-1];
        
        for(int i = 0; i < A.length; i++)
        {
            sorted[scoretable[A[i]] - 1] = A[i];
            scoretable[A[i]]--;
        }
        
        for(int i = 0; i < A.length; i++)
            A[i] = sorted[i];

        chrono[6] = (int)(System.currentTimeMillis() - timer);
    }



    public void RadixSort(int A[]) {
        //WRITE YOUR RADIX SORT CODE HERE

        long timer = System.currentTimeMillis();
        int maxvalue = A[0];
        for(int i = 0; i < A.length; i++)
        {  
            if(A[i] > maxvalue)
                maxvalue = A[i]; 
        }

        for(int exponent = 1;
            maxvalue/exponent > 0;
            exponent *= 10)
        {
            int[] scoretable = new int[maxvalue+1];
            int[] sorted = new int[A.length];

            for(int i = 0; i < A.length; i++)
                scoretable[(A[i]/exponent) % 10] += 1;
            
            for(int i = 1; i <= maxvalue; i++)
                scoretable[i] += scoretable[i-1];
            
            for(int i = A.length-1; i >= 0; i--)
            {
                sorted[scoretable[(A[i]/exponent) % 10] - 1] = A[i];
                scoretable[(A[i]/exponent) % 10]--;
            }
            
            for(int i = 0; i < A.length; i++)
                A[i] = sorted[i];
        }
        chrono[7] = (int)(System.currentTimeMillis() - timer);
    }

    public void summaryofSortingAlgorithms(){
        System.out.println("*****Summary of Sorting Algorithms*****");
//WRITE YOUR SUMMARY HERE

        String[] names = {
            "Bubble Sort", "Selection Sort", "Insertion Sort",
            "Merge Sort", "Quick Sort", "Heap Sort",
            "Counting Sort", "Radix Sort"};
        

        System.out.println("\nComparison-Based Algorithms:");
        System.out.printf("    %s took %dms\n", names[0], chrono[0]);
        System.out.printf("    %s took %dms\n", names[1], chrono[1]);
        System.out.printf("    %s took %dms\n", names[2], chrono[2]);
        
        System.out.println("\nDivide & Conquer Algorithms:");
        System.out.printf("    %s took %dms\n", names[3], chrono[3]);
        System.out.printf("    %s took %dms\n", names[4], chrono[4]);
        System.out.printf("    %s took %dms\n", names[5], chrono[5]);
        
        System.out.println("\nLinear-Time Algorithms:");
        System.out.printf("    %s took %dms\n", names[6], chrono[6]);
        System.out.printf("    %s took %dms\n", names[7], chrono[7]);

        System.out.println("\n---BORA OZDOGAN---\n");


        int ave;
        ave = (chrono[0]+chrono[1]+chrono[2]) / 3;
        System.out.println("Comparison-based algorithms took "+ave+"ms on average");
        ave = (chrono[3]+chrono[4]+chrono[5]) / 3;
        System.out.println("Divide-and-conquer algorithms took "+ave+"ms on average");
        ave = (chrono[6]+chrono[7]) / 2;
        System.out.println("Linear-time algorithms took "+ave+"ms on average");


        int min = 0;
        for(int i = 0; i < 8; i++)
            if(chrono[min] > chrono[i])
                min = i;
        
        int min2 = -1, nMin = 1;
        for(int i = 0; i < 8; i++)
            if(chrono[i]==chrono[min] && i != min)
            {
                min2 = i;
                nMin++;
            } 

        System.out.print("\nFor given inpud, the most succesful ");
        if(nMin > 1)
        {
            System.out.print("algorithms are "+names[min]+" and "+names[min2]);
            if(nMin > 2)
                System.out.print(" (and "+(nMin-2)+" more)");
        }
        else
            System.out.print("algorithm is "+names[min]);
        if(chrono[min] == 0)
            System.out.print(" (which executed almost instantly!)");
        System.out.println();

        System.out.println("\n*****End of Summary of Sorting Algorithms*****\n");
    }
}
