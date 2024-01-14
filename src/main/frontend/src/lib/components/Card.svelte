<script lang="ts">
    export let title: string;
</script>

<div class="card w-full sm:w-96 max-w-sm flex bg-base-100 shadow-xl">
    <div class="card-body">
        <h2 class="card-title">{title}</h2>

        <slot name="content">
        </slot>

        <div class="card-actions justify-end">
            <slot name="actions">
            </slot>
        </div>
    </div>
</div>