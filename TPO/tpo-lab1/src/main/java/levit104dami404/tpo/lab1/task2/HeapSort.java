package levit104dami404.tpo.lab1.task2;

public class HeapSort {
    private HeapSort() {
    }

    public static int[] sort(int[] arr) {
        int n = arr.length;

        // Построение кучи
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            // Текущий корень в конец
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }

        return arr;
    }

    // Преобразование в двоичную кучу поддерева с корневым узлом i; i - индекс в arr[]; n - размер кучи
    private static void heapify(int[] arr, int n, int i) {
        int largest = i; // Наибольший элемент - корень
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest])
            largest = left;

        if (right < n && arr[right] > arr[largest])
            largest = right;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }

}
