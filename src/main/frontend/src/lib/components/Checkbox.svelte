<script lang="ts">
    export let value: string = "", label: string = "", checked: boolean = false, groupValues: string[] = [];

    function change() {
        if (!groupValues.includes(value)) {
            groupValues.push(value);
        } else {
            groupValues.splice(groupValues.indexOf(value), 1);
        }
    }

    $: {
        checked = groupValues.includes(value);
    }
</script>

<div class="block mt-4">
    <label class="flex items-center">
        <input
                type=checkbox
                class="checkbox"
                value={value}
                bind:checked={checked}
                on:click={() => change()}
        />
        <span class="ml-2 text-sm select-none text-gray-600">{ label }</span>
    </label>
</div>
