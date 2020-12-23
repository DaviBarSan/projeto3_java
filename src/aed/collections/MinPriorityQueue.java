package collections;

import sorting.Sort;

import java.util.Arrays;
import java.util.Random;


public class MinPriorityQueue<T extends Comparable<T>> extends Sort {
    //minimum size that an array for the PriorityQueue should have
    private static final int MIN_MAXSIZE = 64;

    public int position, size, maxSize;
    private Comparable<T>[] heapQueue;

    @SuppressWarnings("unchecked")
    public MinPriorityQueue() {
        this.position = 1;
        this.size = 0;
        this.maxSize = MIN_MAXSIZE;
        this.heapQueue = (T[]) new Comparable[maxSize + 1];


    }

    @SuppressWarnings("unchecked")
    public MinPriorityQueue(T[] a) {
        this.position = 1;
        this.size = 0;
        this.maxSize = a.length*2;
        this.heapQueue = (T[]) new Comparable[maxSize+1];
        for (int i = 0; a.length > i; i++) {
            insert(a[i]);
        }

    }

    @SuppressWarnings("unchecked")
    public MinPriorityQueue(int initialMaxSize) {
        if (initialMaxSize > MIN_MAXSIZE+1) {

            this.heapQueue = (T[]) new Comparable[initialMaxSize + 1];
            this.maxSize = initialMaxSize;
            this.size = 0;
            this.position = 1;
        } else {
            this.position = 1;
            this.size = 0;
            this.maxSize = MIN_MAXSIZE;
            this.heapQueue = (T[]) new Comparable[maxSize + 1];
        }
    }



    public MinPriorityQueue<T> clone() {
        MinPriorityQueue<T> result = new MinPriorityQueue<T>(this.maxSize);
        result.size = this.size;
        result.heapQueue = this.heapQueue.clone();

        return result;
    }


    //needed for testing purposes only
    public Comparable[] getElements() {
        return heapQueue;
    }


    public static <T extends Comparable<T>> boolean isMinHeap(T[] a, int n) {
        int lastParent = n / 2;
        int root = 1;
        int checkedIndex = heapCheck(a, root, n);

        if (checkedIndex == lastParent) return true;
        else return false;


    }

    private static <T extends Comparable<T>> int heapCheck(T[] a, int curr, int n) {
        //método que avança o ponteiro até o ultimo nó pai que segue propriedades da minHeap e retorna-o

        if (curr == n / 2) return curr;

        if (less(a[curr], a[curr * 2]) && a[curr * 2 + 1] != null)
        {
            if (less(a[curr], a[(curr * 2) + 1]))
                return heapCheck(a, curr + 1, n);

        }
        return curr;
    }
    @SuppressWarnings("unchecked")
    public void insert(T t) {
        int root = 1;

        if (size/2 == maxSize) {    //size+1 uma vez que o indice 0 do array não foi contabilizado como elemento
            resize((T[]) heapQueue, 2*maxSize);

        }
        heapQueue[position++] = t;
        if (position - 1 > root)
            heapfyBottomUp(heapQueue, position - 1);
        size++;



    }

    @SuppressWarnings("unchecked")
    private void resize(T[] a, int newSize) {

        //criar novo array como resizedHeap
        MinPriorityQueue resizedHeap;
        resizedHeap = new MinPriorityQueue(newSize);

        //passar elementos como
        for (int i = 0; i <= size; i++) {
            resizedHeap.heapQueue[i] = a[i];

        }

        this.heapQueue = resizedHeap.heapQueue;
        this.maxSize = newSize;

    }
    @SuppressWarnings("unchecked")
    public T peekMin() {
        T min = (T) this.heapQueue[1];
        return min;

    }
    @SuppressWarnings("unchecked")
    public T removeMin() {

        if (size == 0)
            return null;

        //guardar valor de min, trocar o last com o first element, set last como null;
        T min = (T) heapQueue[1];
        exchange(heapQueue, 1, position-1);
        heapQueue[position-1] = null;

        //recuar ponteiro e diminuir valor de size;
        size--;
        position--;

        heapfyTopDown(heapQueue, 1, position-1);

        //se maxSize for maior que MinMaxSize = "o heap ja sofreu resize" ; e o nº de elementos for menor que 32;
        //fazer resize para MinMaxSize
        if (maxSize > MIN_MAXSIZE && size < MIN_MAXSIZE/2)
            resize((T[]) heapQueue, MIN_MAXSIZE+1);

        //caso o heap ja tenha sofrido resize e size seja menor que o maxSize/4, fazer resize para metade do tamanho
        else if (maxSize > MIN_MAXSIZE && size < maxSize/4)
            resize((T[]) heapQueue, maxSize/2);

        return min;
    }

    public boolean isEmpty() {
        if (size == 0) return true;
        else return false;

    }

    public int size() {
        return size;
    }

    //needed for testing purposes
    public int getMaxSize() {
        return maxSize;
    }

    private void heapfyBottomUp(Comparable[] a, int k) //k points to last element inserted
    {
        while (k > 1 && less(a[k], a[k / 2])) {
            exchange(a, k / 2, k);
            k = k / 2;
        }
    }

    private void heapfyTopDown(Comparable[] a, int k, int n){
        //n = last valid heap index;
        int child = k*2;
        while (child <= n)
        {
            //set the pointer "child" to the biggest child
            if (child < n && less(a[child+1], a[child]))
                child++;
            if(!less(a[child], a[k])) break;
            exchange(a, k, child);
            k = child;
            child = k*2;
        }
    }

    private boolean isLeaf(int checkIndex) {
        int currentLast = position - 1;
        int lastParent = currentLast / 2;
        return (checkIndex > lastParent && checkIndex <= currentLast);
    }

    public static void main(String[] args) {

       Random r = new Random();

       Integer[] testArray = new Integer[257];
       for (int i = 0; i < testArray.length; i++)
           testArray[i] = r.nextInt(20);

       MinPriorityQueue testHeap = new MinPriorityQueue(testArray);

       System.out.println(testHeap.getMaxSize());
       System.out.println(Arrays.toString(testHeap.getElements()));
       System.out.println(testHeap.size());

       for (int i = 0; i < 130; i++) {
           testHeap.removeMin();
           System.out.println(testHeap.size());
       }


        System.out.println(testHeap.size());
        System.out.println(testHeap.getMaxSize());
        System.out.println(Arrays.toString(testHeap.getElements()));




       //fazer o resize caso o array seja muito menor que o maxSize;
        //elements size = tamanho do array contando com o index 0;

     }
}











